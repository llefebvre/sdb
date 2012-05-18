package com.datagardens.nq.sdb.client.views;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdfviewer.PageDrawer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.TextPosition;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.ole.win32.OleClientSite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.datagardens.nq.sdb.client.SWTResourceManager;

public class NavigationView extends ViewPart {

	public static final String ID = "com.datagardens.nq.sdb.client.views.navigationView";
	
	private TreeViewer navigationViewer;
	
	public NavigationView()
	{
		
	}

	@Override
	public void createPartControl(Composite parent) 
	{
		
		PDDocument doc = null;
		PageDrawer drawer = null;
		Dimension pageDimension = null;
		
		OleClientSite site = null;
		
		Color gray1 = SWTResourceManager.getColor(230,230,230);
		Color gray2 = SWTResourceManager.getColor(215,215,215);
		Color gray3 = SWTResourceManager.getColor(200,200,200);
		
		Color blue = SWTResourceManager.getColor(SWT.COLOR_BLUE);
		Color white = SWTResourceManager.getColor(SWT.COLOR_WHITE);
		Color black = SWTResourceManager.getColor(0,0,0);
		Font bold = SWTResourceManager.getFont(
				parent.getFont().getFontData()[0].getName(),
				parent.getFont().getFontData()[0].getHeight(),
				SWT.BOLD);
		
		
		try
		{
			Composite com = new Composite(parent, SWT.NONE);
//			Composite com = new Composite(parent, SWT.EMBEDDED | SWT.NO_BACKGROUND);
			com.setLayout(new FillLayout());
//			com.setLayout(null);
			
//			SawSheetView sawSheet = new SawSheetView(com, SWT.BORDER);
			
			
//			com.setBackground(white);
			
			/*Grid grid = new Grid(com, SWT.BORDER);
			grid.setCellSelectionEnabled(true);
			
			GridColumn colA = new GridColumn(grid, SWT.NONE);
			colA.setWidth(120);
			colA.setAlignment(SWT.CENTER);
			GridColumn colB = new GridColumn(grid, SWT.NONE);
			colB.setWidth(120);
			colB.setAlignment(SWT.CENTER);
			GridColumn colC = new GridColumn(grid, SWT.NONE);
			colC.setWidth(120);
			colC.setAlignment(SWT.CENTER);
			GridColumn colD = new GridColumn(grid, SWT.NONE);
			colD.setWidth(120);
			colD.setAlignment(SWT.CENTER);
			
			 headers 
			GridItem headers = new GridItem(grid, SWT.NONE);
			headers.setColumnSpan(2, 2);
			
			headers.setText(0, "Job#");
			headers.setText(1, "Job Qty");
			headers.setText(2, "Total Pieces Cut (Overall Yield)");
			
			headers.setBackground(0, gray1);
			headers.setBackground(1, gray1);
			headers.setBackground(2, gray1);
			
			headers.setFont(SWTResourceManager.getFont(
					headers.getFont().getFontData()[0].getName(),
					headers.getFont().getFontData()[0].getHeight(),
					SWT.BOLD));
			
			 headers values 
			GridItem headersValues = new GridItem(grid, SWT.NONE);
			headersValues.setColumnSpan(2, 2);
			headersValues.setText(0, "25612-000");
			headersValues.setText(1, "20");
			headersValues.setForeground(blue);
			headersValues.setFont(SWTResourceManager.getFont(
					headersValues.getFont().getFontData()[0].getName(),
					headersValues.getFont().getFontData()[0].getHeight(),
					SWT.BOLD));
			
			
			 other info 
			GridItem info = new GridItem(grid, SWT.NONE);
			info.setText(0, "Part #");
			info.setText(1, "H036792800");
			info.setText(2, "Part Name:");
			info.setText(3, "Piston Mandel, P110");
			
			info.setBackground(0,gray1);
			info.setBackground(2,gray1);
			
			info.setForeground(1,blue);
			info.setForeground(3,blue);
			
			info.setFont(SWTResourceManager.getFont(
					info.getFont().getFontData()[0].getName(),
					info.getFont().getFontData()[0].getHeight(),
					SWT.BOLD));
			*/
			
			
			
			
			/*Grid grid = new Grid(com, SWT.NONE);
			grid.setCellSelectionEnabled(true);
			GridColumn [] colms = createColumns(grid, 8, 80);
			grid.setLinesVisible(true);
			grid.setLineColor(SWTResourceManager.getColor(SWT.COLOR_BLACK));
			
			GridItem sheetInfoTitles = new GridItem(grid, SWT.NONE);
			
			sheetInfoTitles.setColumnSpan(0, 1);
			sheetInfoTitles.setColumnSpan(2, 1);
			sheetInfoTitles.setColumnSpan(4, 3);
			
			sheetInfoTitles.setText(0, "Job #");
			sheetInfoTitles.setText(2, "Job Qty");
			sheetInfoTitles.setText(4, "Total Pieces Cut (Overall Yield)");
			
			sheetInfoTitles.setBackground(0,gray1);
			sheetInfoTitles.setBackground(2,gray1);
			sheetInfoTitles.setBackground(4,gray1);
			sheetInfoTitles.setFont(bold);
			
			
			GridItem sheetInfoValues = new GridItem(grid, SWT.NONE);
			sheetInfoValues.setColumnSpan(0, 1);
			sheetInfoValues.setColumnSpan(2, 1);
			sheetInfoValues.setColumnSpan(4, 3);
			
			sheetInfoValues.setText(0, "25612-0000");
			sheetInfoValues.setText(2, "20");
			sheetInfoValues.setForeground(blue);
			sheetInfoValues.setFont(bold);
			
			GridItem jobInfo = new GridItem(grid, SWT.NONE);
			jobInfo.setColumnSpan(1, 1);
			jobInfo.setColumnSpan(3, 1);
			jobInfo.setColumnSpan(5, 2);
			
			jobInfo.setText(0, "Part #:");
			jobInfo.setText(1, "H036792800");
			jobInfo.setText(3, "Part Name:");
			jobInfo.setText(5, "Piston Mandrel, P110");
			
			jobInfo.setBackground(0, gray1);
			jobInfo.setBackground(3, gray1);
			jobInfo.setForeground(1, blue);
			jobInfo.setForeground(5, blue);
			
			jobInfo.setFont(bold);
			
			
			                
			
			GridItem space = new GridItem(grid, SWT.NONE);
			space.setColumnSpan(0, 7);
			space.setRowSpan(0, 1);
			GridItem space1 = new GridItem(grid, SWT.NONE);
			space1.setColumnSpan(0, 7);
			
			------------------------------
			GridItem inputTitle = new GridItem(grid, SWT.NONE);
			inputTitle.setText("Multiple Part Cuts");
			inputTitle.setColumnSpan(0, 7);
			inputTitle.setBackground(0, gray2);
			inputTitle.setFont(bold);
			
			
			GridItem inputColumnNames = new GridItem(grid, SWT.NONE);
			inputColumnNames.setText(0, "S/N's");
			inputColumnNames.setText(2, "Emp #");
			inputColumnNames.setText(3, "MH Initial");
			inputColumnNames.setText(4, "Yield Cut");
			inputColumnNames.setText(5, "Date (mo/day)");
			inputColumnNames.setText(6, "Heat Number");
			
			inputColumnNames.setColumnSpan(0, 1);
			inputColumnNames.setColumnSpan(6, 1);
			inputColumnNames.setRowSpan(2, 1);
			inputColumnNames.setRowSpan(3, 1);
			inputColumnNames.setRowSpan(4, 1);
			inputColumnNames.setRowSpan(5, 1);
			inputColumnNames.setRowSpan(6, 1);
			
			inputColumnNames.setBackground(0, gray1);
			inputColumnNames.setBackground(2, gray1);
			inputColumnNames.setBackground(3, gray1);
			inputColumnNames.setBackground(4, gray1);
			inputColumnNames.setBackground(5, gray1);
			inputColumnNames.setBackground(6, gray1);
			inputColumnNames.setFont(bold);
			

			GridItem cuts = new GridItem(grid, SWT.NONE);
			cuts.setText(0,"First Cut");
			cuts.setText(1,"Last Cut");
			cuts.setBackground(0, gray2);
			cuts.setBackground(1, gray2);
			cuts.setFont(bold);
			
			
			----------------
			GridItem line1 = new GridItem(grid, SWT.NONE);
			line1.setText(0, "1");
			line1.setText(1, "10");
			line1.setText(2, "417");
			
			GridItem line2 = new GridItem(grid, SWT.NONE);
			line2.setText(0, "11");
			line2.setText(1, "20");
			line2.setText(2, "543");
			
			
			 new GridItem(grid, SWT.NONE);
			 new GridItem(grid, SWT.NONE);
			 new GridItem(grid, SWT.NONE);
			 new GridItem(grid, SWT.NONE);
			 new GridItem(grid, SWT.NONE);
			 new GridItem(grid, SWT.NONE);
			 new GridItem(grid, SWT.NONE);
			 new GridItem(grid, SWT.NONE);
			 new GridItem(grid, SWT.NONE);
			 new GridItem(grid, SWT.NONE);
			 new GridItem(grid, SWT.NONE);
			 new GridItem(grid, SWT.NONE);
			 new GridItem(grid, SWT.NONE);
			 new GridItem(grid, SWT.NONE);
			 new GridItem(grid, SWT.NONE);
			 new GridItem(grid, SWT.NONE);
			 new GridItem(grid, SWT.NONE);*/
			
			
			
			
			
			/*Composite pdfComposite = new Composite(com, SWT.BORDER);
			pdfComposite.setLayout(null);*/
			
			
			
			
			/* SWT */
			
			/*
			File file = new File ("/sawsheet.pdf");
			doc = PDDocument.load(file);
			
			List<PDPage> pages = new ArrayList<PDPage>();
			doc.getDocumentCatalog().getPages().getAllKids(pages);
			PDPage page = pages.get(0);
			
			
			
			int w = page.findMediaBox().createDimension().width;
			int h = page.findMediaBox().createDimension().height;
			
			BufferedImage bimg = page.convertToImage(BufferedImage.SCALE_SMOOTH, 72);
			
			 SWT 			
			Image image = new Image(PlatformUI.getWorkbench().getDisplay(),
					convertToSWTImage(bimg));
			

			PDAcroForm form = doc.getDocumentCatalog().getAcroForm();
			for(Object fObj : form.getFields())
			{
				if(fObj instanceof PDTextbox)
				{
					PDTextbox field = (PDTextbox) fObj;
					PDRectangle rect = field.getWidget().getRectangle();
					
					Text text = new Text(pdfComposite, SWT.NONE);
					
					int x = (int) rect.getLowerLeftX();
					int y = (int) (bimg.getHeight() - rect.getUpperRightY());
					
					text.setBounds(x, y, (int)rect.getWidth(), (int)rect.getHeight());
					text.setText(field.getPartialName());
//					text.setFont(new Font(PlatformUI.getWorkbench().getDisplay(), "Arial", 12, SWT.BOLD));
				}
				
			}
			
			Composite dataComposite = new Composite(pdfComposite, SWT.BORDER);
			dataComposite.setBounds(0, 80, bimg.getWidth(), 500);
			
			GridLayout dataLayout = new GridLayout(7, false);
			dataLayout.horizontalSpacing=0;
			dataLayout.verticalSpacing=0;
			dataLayout.marginWidth=0;
			dataLayout.marginHeight=0;
			
			dataComposite.setLayout(new FillLayout());
			
			new Text(dataComposite, SWT.BORDER).setText("Aaaaaaaaaaaaaaaaaa");
			
			for(int i=0; i<10; i++)
			{
				System.out.println("creating text");
				Text text= new Text(dataComposite, SWT.BORDER);
				text.setText("a=" + i);
				text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false , false));
			}
			
			
			Label label = new Label(pdfComposite, SWT.NONE);
			label.setBounds(0, 0, bimg.getWidth(), bimg.getHeight());
			label.setImage(image);*/
			
			
			
			
			
			/* SWING */
			
			/*Frame frame = SWT_AWT.new_Frame(com);
//			frame.add();
			
			JPanel panel = new JPanel();
			panel.setLayout(null);
			
			
			JLabel label2 = new JLabel("SOME TEXT");
			label2.setBounds(30,30,100,200);
			panel.add(label2);
			
			int w = page.findMediaBox().createDimension().width;
			int h = page.findMediaBox().createDimension().height;
			
			ImageIcon icon = new ImageIcon(page.convertToImage().getScaledInstance(w, h,
					BufferedImage.SCALE_SMOOTH));
			JLabel label = new JLabel(icon);
			label.setBounds(0,0,w,h);
			panel.add(label);
			
			frame.add(panel);*/
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
//				doc.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
			
		/*OleFrame frame = new OleFrame(com, SWT.NONE);
		Menu bar = new Menu(frame);
		MenuItem item = new MenuItem(bar, SWT.CASCADE);
		item.setText("item");
		
		System.out.println(">>>" + OLE.findProgramID("doc"));
		
		frame.setMenu(bar);
		File file = new File ("/sawsheet.pdf");
		OleClientSite site = new OleClientSite(frame, SWT.NONE, file);*/
		

		/*navigationViewer = new TreeViewer(parent);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(navigationViewer.getControl(), ID);*/
	}

	
	private GridColumn[] createColumns(Grid grid, int total, int width) 
	{
		GridColumn [] colms = new GridColumn[total];
		
		for(int i=0; i<total; i++)
		{
			GridColumn col = new GridColumn(grid, SWT.NONE);
			col.setWidth(width);
			colms[i] = col;
			col.setAlignment(SWT.CENTER);
			col.setWordWrap(true);
		}
		
		return colms;
	}


	private class TextLocations extends PDFTextStripper 
	{

		public TextLocations() throws IOException {
			super.setSortByPosition(true);
		}
		
		
		public void print(PDDocument document) 
				throws IOException
		{
			@SuppressWarnings("rawtypes")
			List allPages = document.getDocumentCatalog().getAllPages();
            for( int i=0; i<allPages.size(); i++ )
            {
                PDPage page = (PDPage)allPages.get( i );
                PDStream contents = page.getContents();
                if( contents != null )
                {
                    processStream( page, page.findResources(), page.getContents().getStream() );
                }
            }

		}

	    protected void processTextPosition( TextPosition text )
	    {
	        System.out.println( "String[" + text.getXDirAdj() + "," +
	                text.getYDirAdj() + " fs=" + text.getFontSize() + " xscale=" +
	                text.getXScale() + " height=" + text.getHeightDir() + " space=" +
	                text.getWidthOfSpace() + " width=" +
	                text.getWidthDirAdj() + "]" + text.getCharacter() );
	    }
		
	}
	
	public ImageData convertToSWTImage(BufferedImage bufferedImage) {
		if (bufferedImage.getColorModel() instanceof DirectColorModel) {
			DirectColorModel colorModel = (DirectColorModel)bufferedImage.getColorModel();
			PaletteData palette = new PaletteData(colorModel.getRedMask(), colorModel.getGreenMask(), colorModel.getBlueMask());
			ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(), colorModel.getPixelSize(), palette);
			for (int y = 0; y < data.height; y++) {
				for (int x = 0; x < data.width; x++) {
					int rgb = bufferedImage.getRGB(x, y);
					int pixel = palette.getPixel(new RGB((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF)); 
					data.setPixel(x, y, pixel);
					if (colorModel.hasAlpha()) {
						data.setAlpha(x, y, (rgb >> 24) & 0xFF);
					}
				}
			}
			return data;		
		} else if (bufferedImage.getColorModel() instanceof IndexColorModel) {
			IndexColorModel colorModel = (IndexColorModel)bufferedImage.getColorModel();
			int size = colorModel.getMapSize();
			byte[] reds = new byte[size];
			byte[] greens = new byte[size];
			byte[] blues = new byte[size];
			colorModel.getReds(reds);
			colorModel.getGreens(greens);
			colorModel.getBlues(blues);
			RGB[] rgbs = new RGB[size];
			for (int i = 0; i < rgbs.length; i++) {
				rgbs[i] = new RGB(reds[i] & 0xFF, greens[i] & 0xFF, blues[i] & 0xFF);
			}
			PaletteData palette = new PaletteData(rgbs);
			ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(), colorModel.getPixelSize(), palette);
			data.transparentPixel = colorModel.getTransparentPixel();
			WritableRaster raster = bufferedImage.getRaster();
			int[] pixelArray = new int[1];
			for (int y = 0; y < data.height; y++) {
				for (int x = 0; x < data.width; x++) {
					raster.getPixel(x, y, pixelArray);
					data.setPixel(x, y, pixelArray[0]);
				}
			}
			return data;
		}
		return null;
	}
	
	@Override
	public void setFocus() {
	}

}
