package com.datagardens.nq.sdb.client;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaPrintableArea;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import com.datagardens.nq.sdb.commons.model.IndividualCut;
import com.datagardens.nq.sdb.commons.model.Job;
import com.datagardens.nq.sdb.commons.model.NQDataModel;
import com.datagardens.nq.sdb.commons.model.NQModelObject;
import com.datagardens.nq.sdb.commons.model.SawSheet;
import com.datagardens.nq.sdb.commons.model.ShiftLogSheet;

public class SheetPrinter 
{
	private Job job;
	private NQModelObject sheet;
	private boolean printBlank;
	private int shiftIndex;
	
	public SheetPrinter(Job job, NQModelObject sheet, boolean printBlank, int shiftIndex)
	{
		this.sheet = sheet;
		this.job = job;
		this.printBlank = printBlank;
	}
	
	public SheetPrinter(Job job, NQModelObject sheet, int shiftIndex)
	{
		this(job, sheet, false, shiftIndex);
	}
	
	public void print()
	throws Exception 
	{
		
		if(sheet instanceof SawSheet)
		{
			SawSheet sawSheet = (SawSheet) sheet;
			
			File file = new File("c:\\pdfs\\saw_sheet.pdf");
			
			PDDocument doc = PDDocument.load(file);
			List<PDPage> pages = new ArrayList<PDPage>();
			doc.getDocumentCatalog().getPages().getAllKids(pages);
			
			PDPage page = pages.get(0);
			PDFont font = PDType1Font.HELVETICA;
			PDPageContentStream pageStream = new PDPageContentStream(doc, page, true, false);
			
			pageStream.setFont(font, 8);
			
			addText(400f, 660f , pageStream, "Shift " + shiftIndex+1);
			
			addText(76.5f, 608f , pageStream, sawSheet.getJobId());
			addText(191.5f, 608f , pageStream, job.getQuantity());
			
			if(!printBlank)
			{
				addText(307f, 608f , pageStream, sawSheet.getQuantityBlanks());
				addText(421f, 608f , pageStream, sawSheet.getBlankLength());
				addText(537f, 608f , pageStream, sawSheet.getYieldPerBlank());
			}
			
			addText(130f, 593f , pageStream, job.getPartNumber());
			addText(330f, 593f , pageStream, job.getPartDesc());
			
			if(!printBlank)
			{
				addText(560, 593f , pageStream, sawSheet.getPlannedNetYield());
			}

			if(!printBlank)
			{
				double Y = -1f;
				int i = 0;
//				for(double i=0; i<sawSheet.getCuts().size(); i++)
				for(IndividualCut cut : 
					NQDataModel.getCutsForSawSheetShift(sawSheet, shiftIndex).values())
				{
					/*IndividualCut cut = sawSheet.getCuts().get(i);*/
					
					Y = 510 - (12.362*i);
					
					for(int j=0; j<9; j++)
					{
						switch(j)
						{
						case 0:
							addText(47f, (float)Y , pageStream, cut.getCutLength());
						break;
						
						case 1:
							addText(104f, (float)Y , pageStream, cut.getYieldCut());
							break;
							
						case 2:
							addText(161.5f, (float)Y , pageStream, cut.getSerialNumberFirstCut());
							break;
							
						case 3:
							addText(220f, (float)Y , pageStream, cut.getSerialNumberLastCut());
							break;
							
						case 4:
							addText(277.5f, (float)Y , pageStream, cut.getEmpNumber());
							break;
							
						case 5:
							addText(325f, (float)Y , pageStream, cut.isSigned());
							break;
							
						case 6:
							addText(393f, (float)Y , pageStream, cut.getDate());
							break;
							
						case 7:
							addText(451f, (float)Y , pageStream, cut.getnI());
							break;
							
						case 8:
							addText(537f, (float)Y , pageStream, cut.getHeatNumber());
							break;
							
						default:
							break;
						}
					}
					
					i++;
				}
			}
			
			pageStream.close();
			doc.save("c:\\pdfs\\test.pdf");
			
			if(false)
			{
				BufferedImage bufferedImage = pages.get(0).convertToImage();
				
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
				ImageWriter iw = ImageIO.getImageWritersByFormatName("JPG").next();
				iw.setOutput(ios);
				iw.write(bufferedImage);
				
				byte[] imageBytesArray = baos.toByteArray();
				
				
				DocFlavor flavor = DocFlavor.BYTE_ARRAY.JPEG;
		        PrintService [] services = PrintServiceLookup.lookupPrintServices(flavor, null);
		        System.out.println("Total services available = " + services.length);
				
		        int serviceIndex = 0;
		        for (int i = 0; i < services.length; i++)
		        {
		        	serviceIndex = i;
		        	
		    		/*if(services [i].getName().indexOf("HP Color LaserJet CM3530") != -1)
		    		{
		    			  System.out.println("service found, " + services[i].getName());
		    			break;
		    		}*/
		    		
		    		if(services [i].getName().indexOf("HP Officejet Pro L7700") != -1)
		    		{
		    			  System.out.println("service found, " + services[i].getName());
		    			break;
		    		}
		        }
		        
		        System.out.println("printing to " + services[serviceIndex].getName());
		        DocPrintJob printJob = services[serviceIndex].createPrintJob();
		        Doc documnet = new SimpleDoc(imageBytesArray, flavor, null);
		        PrintRequestAttributeSet reqAttributeSet = new HashPrintRequestAttributeSet();
		        reqAttributeSet.add(new MediaPrintableArea(0f, 0f, 8.27f, 11.69f, MediaPrintableArea.INCH));
		        reqAttributeSet.add(new Copies(1));
		        printJob.print(documnet, reqAttributeSet);
			}
			
	        
		}
		else if (sheet instanceof ShiftLogSheet)
		{
			File file = new File("c:\\pdfs\\shift_log_sheet.pdf");
			
			PDDocument doc = PDDocument.load(file);
			List<PDPage> pages = new ArrayList<PDPage>();
			doc.getDocumentCatalog().getPages().getAllKids(pages);
			
			PDPage page = pages.get(0);
			PDFont font = PDType1Font.HELVETICA;
			PDPageContentStream pageStream = new PDPageContentStream(doc, page, true, false);
			
			pageStream.setFont(font, 10);
			
			pageStream.close();
			doc.save("c:\\pdfs\\test_shift.pdf");
			
			
			/*BufferedImage bufferedImage = pages.get(0).convertToImage();
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
			ImageWriter iw = ImageIO.getImageWritersByFormatName("JPG").next();
			iw.setOutput(ios);
			iw.write(bufferedImage);
			
			byte[] imageBytesArray = baos.toByteArray();
			
			
			DocFlavor flavor = DocFlavor.BYTE_ARRAY.JPEG;
	        PrintService [] services = PrintServiceLookup.lookupPrintServices(flavor, null);
	        System.out.println("Total services available = " + services.length);
			
	        int serviceIndex = 0;
	        for (int i = 0; i < services.length; i++)
	        {
	        	serviceIndex = i;
	        	
	    		if(services [i].getName().indexOf("HP Color LaserJet CM3530") != -1)
	    		{
	    			  System.out.println("service found, " + services[i].getName());
	    			break;
	    		}
	        }
	        
	        System.out.println("printing to " + services[serviceIndex].getName());
	        DocPrintJob printJob = services[serviceIndex].createPrintJob();
	        Doc documnet = new SimpleDoc(imageBytesArray, flavor, null);
	        PrintRequestAttributeSet reqAttributeSet = new HashPrintRequestAttributeSet();
	        reqAttributeSet.add(new MediaPrintableArea(0f, 0f, 8.27f, 11.69f, MediaPrintableArea.INCH));
	        reqAttributeSet.add(new Copies(1));
	        printJob.print(documnet, reqAttributeSet);*/
			
			
		}
	}

	private void addText(float centerX, float centerY, 
			PDPageContentStream pageStream, int text) 
	throws IOException 
	{
		addText(centerX, centerY, pageStream, text+"");
	}
	
	private void addText(float centerX, float centerY, 
			PDPageContentStream pageStream, boolean value) 
	throws IOException 
	{
		addText(centerX, centerY, pageStream, value?"YES":"NO");
	}
	
	private void addText(float centerX, float centerY, 
			PDPageContentStream pageStream, String text) 
	throws IOException 
	{
		pageStream.beginText();
		float newX = getTextLocationFromCenter(centerX, text);
		pageStream.moveTextPositionByAmount(newX, centerY);
		pageStream.drawString(text);
		pageStream.endText();
		
	}

	private float getTextLocationFromCenter(float centerX, String text) 
	{
		float textLengh = (float)text.length();
		float x = centerX - ((4.2f * textLengh)/2);
		return x;
	}
}
