package com.confsync.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

/**
 * Utility for generating official PDF certificates using Apache PDFBox.
 * Demonstrates Java File Handling and Document Processing.
 */
@Component
public class CertificatePdfGenerator {

    public void generateCertificatePdf(File destinationFile,
                                       String participantName,
                                       String conferenceTitle,
                                       String conferenceDates,
                                       String certificateNumber,
                                       String verificationCode) throws IOException {

        // Landscape dimensions (US Letter: 792 x 612)
        PDRectangle landscape = new PDRectangle(792, 612);
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(landscape);
            document.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(document, page)) {
                // Outer Border
                cs.setLineWidth(4f);
                cs.setStrokingColor(26 / 255f, 86 / 255f, 219 / 255f); // Deep Professional Blue
                cs.addRect(25, 25, 742, 562);
                cs.stroke();

                // Inner Border
                cs.setLineWidth(1.5f);
                cs.setStrokingColor(180 / 255f, 198 / 255f, 252 / 255f);
                cs.addRect(35, 35, 722, 542);
                cs.stroke();

                // Fonts
                PDType1Font fontTitle = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
                PDType1Font fontSub = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
                PDType1Font fontName = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
                PDType1Font fontSmall = new PDType1Font(Standard14Fonts.FontName.HELVETICA_OBLIQUE);

                // Header
                cs.beginText();
                cs.setFont(fontTitle, 26);
                cs.setNonStrokingColor(30 / 255f, 41 / 255f, 59 / 255f);
                cs.newLineAtOffset(210, 510);
                cs.showText("CERTIFICATE OF PARTICIPATION");
                cs.endText();

                // Subtitle
                cs.beginText();
                cs.setFont(fontSub, 13);
                cs.setNonStrokingColor(100 / 255f, 116 / 255f, 139 / 255f);
                cs.newLineAtOffset(290, 475);
                cs.showText("ConfSync Academic & Technical Forum");
                cs.endText();

                // "This is proudly presented to"
                cs.beginText();
                cs.setFont(fontSub, 14);
                cs.setNonStrokingColor(71 / 255f, 85 / 255f, 105 / 255f);
                cs.newLineAtOffset(295, 420);
                cs.showText("This certificate is awarded to:");
                cs.endText();

                // Participant Name
                cs.beginText();
                cs.setFont(fontName, 24);
                cs.setNonStrokingColor(26 / 255f, 86 / 255f, 219 / 255f);
                float nameWidth = (fontName.getStringWidth(participantName) / 1000f) * 24;
                float nameX = (792 - nameWidth) / 2f;
                cs.newLineAtOffset(nameX, 375);
                cs.showText(participantName);
                cs.endText();

                // Underline under participant name
                cs.setLineWidth(1f);
                cs.setStrokingColor(203 / 255f, 213 / 255f, 225 / 255f);
                cs.moveTo(200, 360);
                cs.lineTo(592, 360);
                cs.stroke();

                // Details Text
                cs.beginText();
                cs.setFont(fontSub, 13);
                cs.setNonStrokingColor(51 / 255f, 65 / 255f, 85 / 255f);
                cs.newLineAtOffset(180, 320);
                cs.showText("for active participation and successful completion of the conference:");
                cs.endText();

                // Conference Title
                cs.beginText();
                cs.setFont(fontName, 17);
                cs.setNonStrokingColor(15 / 255f, 23 / 255f, 42 / 255f);
                float confWidth = (fontName.getStringWidth(conferenceTitle) / 1000f) * 17;
                float confX = (792 - confWidth) / 2f;
                cs.newLineAtOffset(confX, 280);
                cs.showText(conferenceTitle);
                cs.endText();

                // Dates
                cs.beginText();
                cs.setFont(fontSub, 12);
                cs.setNonStrokingColor(100 / 255f, 116 / 255f, 139 / 255f);
                cs.newLineAtOffset(320, 245);
                cs.showText("Conducted on: " + conferenceDates);
                cs.endText();

                // Footer lines for signatures
                cs.setLineWidth(1f);
                cs.setStrokingColor(148 / 255f, 163 / 255f, 184 / 255f);
                // Left signature line
                cs.moveTo(100, 140);
                cs.lineTo(260, 140);
                cs.stroke();

                // Right signature line
                cs.moveTo(532, 140);
                cs.lineTo(692, 140);
                cs.stroke();

                // Signature Labels
                cs.beginText();
                cs.setFont(fontSub, 11);
                cs.setNonStrokingColor(71 / 255f, 85 / 255f, 105 / 255f);
                cs.newLineAtOffset(130, 120);
                cs.showText("Program Chair");
                cs.endText();

                cs.beginText();
                cs.setFont(fontSub, 11);
                cs.setNonStrokingColor(71 / 255f, 85 / 255f, 105 / 255f);
                cs.newLineAtOffset(560, 120);
                cs.showText("Organizing Secretary");
                cs.endText();

                // Verification & Certificate metadata
                cs.beginText();
                cs.setFont(fontSmall, 9);
                cs.setNonStrokingColor(148 / 255f, 163 / 255f, 184 / 255f);
                cs.newLineAtOffset(70, 60);
                cs.showText("Certificate ID: " + certificateNumber + "  |  Verification Code: " + verificationCode + "  |  Verified via ConfSync");
                cs.endText();
            }

            // Ensure parent directory exists
            File parentDir = destinationFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            document.save(destinationFile);
        }
    }
}

