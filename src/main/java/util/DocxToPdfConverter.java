package util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.poi.xwpf.usermodel.*;

import java.awt.*;
import java.io.*;
import java.util.List;

public class DocxToPdfConverter {

    private static final float MARGIN = 50;
    private static final float PAGE_WIDTH = PDRectangle.A4.getWidth();
    private static final float PAGE_HEIGHT = PDRectangle.A4.getHeight();
    private static final float CONTENT_WIDTH = PAGE_WIDTH - 2 * MARGIN;

    private PDDocument pdfDocument;
    private PDPage currentPage;
    private PDPageContentStream contentStream;
    private float yPosition;
    private final PDFont defaultFont = PDType1Font.TIMES_ROMAN;
    private final PDFont boldFont = PDType1Font.TIMES_BOLD;
    private final PDFont italicFont = PDType1Font.TIMES_ITALIC;


    public byte[] convert(byte[] docxFileData) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(docxFileData);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        pdfDocument = new PDDocument();
        try (XWPFDocument docxDocument = new XWPFDocument(inputStream)) {

            createNewPage();

            for (IBodyElement element : docxDocument.getBodyElements()) {
                if (element instanceof XWPFParagraph) {
                    processParagraph((XWPFParagraph) element);
                } else if (element instanceof XWPFTable) {
                    processTable((XWPFTable) element);
                }
            }

            contentStream.close();
            pdfDocument.save(outputStream);
        }

        return outputStream.toByteArray();
    }


    private void createNewPage() throws IOException {
        if (contentStream != null) {
            contentStream.close();
        }

        currentPage = new PDPage(PDRectangle.A4);
        pdfDocument.addPage(currentPage);
        contentStream = new PDPageContentStream(pdfDocument, currentPage);
        yPosition = PAGE_HEIGHT - MARGIN;
    }

    private void processParagraph(XWPFParagraph paragraph) throws IOException {
        if (paragraph.getText().trim().isEmpty()) {
            yPosition -= 12; // Add some space for empty paragraphs
            checkPageBreak(12);
            return;
        }

        String alignment = paragraph.getAlignment().toString();
        List<XWPFRun> runs = paragraph.getRuns();

        if (runs.isEmpty()) {
            return;
        }

        float fontSize = 12f;
        float lineHeight = fontSize * 1.2f;

        // Process each run in the paragraph
        StringBuilder lineText = new StringBuilder();
        PDFont currentFont = defaultFont;

        for (XWPFRun run : runs) {
            String text = run.getText(0);
            if (text == null || text.isEmpty()) continue;

            // Determine font based on formatting
            if (run.isBold() && run.isItalic()) {
                currentFont = PDType1Font.HELVETICA_BOLD_OBLIQUE;
            } else if (run.isBold()) {
                currentFont = boldFont;
            } else if (run.isItalic()) {
                currentFont = italicFont;
            } else {
                currentFont = defaultFont;
            }

            // Get font size if specified
            if (run.getFontSize() != -1) {
                fontSize = run.getFontSize();
                lineHeight = fontSize * 1.2f;
            }

            lineText.append(text);
        }

        // Word wrap and draw text
        drawWrappedText(lineText.toString(), currentFont, fontSize, alignment);
        yPosition -= lineHeight;
        checkPageBreak(lineHeight);
    }

    private void drawWrappedText(String text, PDFont font, float fontSize, String alignment) throws IOException {
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        float textWidth = 0;

        for (String word : words) {
            String testLine = line.length() == 0 ? word : line + " " + word;
            float testWidth = font.getStringWidth(testLine) / 1000 * fontSize;

            if (testWidth > CONTENT_WIDTH && line.length() > 0) {
                // Draw current line
                drawTextLine(line.toString(), font, fontSize, alignment);
                yPosition -= fontSize * 1.2f;
                checkPageBreak(fontSize * 1.2f);

                // Start new line with current word
                line = new StringBuilder(word);
            } else {
                line = new StringBuilder(testLine);
            }
        }

        // Draw remaining text
        if (line.length() > 0) {
            drawTextLine(line.toString(), font, fontSize, alignment);
        }
    }

    private void drawTextLine(String text, PDFont font, float fontSize, String alignment) throws IOException {
        float textWidth = font.getStringWidth(text) / 1000 * fontSize;
        float xPosition = MARGIN;

        // Calculate x position based on alignment
        switch (alignment.toUpperCase()) {
            case "CENTER":
                xPosition = (PAGE_WIDTH - textWidth) / 2;
                break;
            case "RIGHT":
                xPosition = PAGE_WIDTH - MARGIN - textWidth;
                break;
            case "LEFT":
            default:
                xPosition = MARGIN;
                break;
        }

        contentStream.beginText();
        contentStream.setFont(font, fontSize);
        contentStream.newLineAtOffset(xPosition, yPosition);
        contentStream.showText(text);
        contentStream.endText();
    }

    private void processTable(XWPFTable table) throws IOException {
        float tableWidth = CONTENT_WIDTH;
        List<XWPFTableRow> rows = table.getRows();

        if (rows.isEmpty()) return;

        // Calculate column widths (simplified - equal width)
        int numCols = rows.get(0).getTableCells().size();
        float cellWidth = tableWidth / numCols;
        float cellHeight = 20f;

        for (XWPFTableRow row : rows) {
            float startX = MARGIN;
            float startY = yPosition - cellHeight;

            checkPageBreak(cellHeight + 5);

            List<XWPFTableCell> cells = row.getTableCells();
            for (int i = 0; i < cells.size(); i++) {
                XWPFTableCell cell = cells.get(i);
                float cellX = startX + (i * cellWidth);

                // Draw cell border
                drawCellBorder(cellX, startY, cellWidth, cellHeight);

                // Draw cell content
                String cellText = cell.getText().trim();
                if (!cellText.isEmpty()) {
                    contentStream.beginText();
                    contentStream.setFont(defaultFont, 10);
                    contentStream.newLineAtOffset(cellX + 2, startY + cellHeight/2 - 3);

                    // Truncate text if too long
                    float maxWidth = cellWidth - 4;
                    if (defaultFont.getStringWidth(cellText) / 1000 * 10 > maxWidth) {
                        while (defaultFont.getStringWidth(cellText + "...") / 1000 * 10 > maxWidth && cellText.length() > 0) {
                            cellText = cellText.substring(0, cellText.length() - 1);
                        }
                        cellText += "...";
                    }

                    contentStream.showText(cellText);
                    contentStream.endText();
                }
            }

            yPosition -= cellHeight + 2;
        }

        yPosition -= 10; // Add space after table
    }

    private void drawCellBorder(float x, float y, float width, float height) throws IOException {
        contentStream.setStrokingColor(Color.BLACK);
        contentStream.setLineWidth(0.5f);
        contentStream.addRect(x, y, width, height);
        contentStream.stroke();
    }

    private void checkPageBreak(float contentHeight) throws IOException {
        if (yPosition - contentHeight < MARGIN) {
            createNewPage();
        }
    }
}
