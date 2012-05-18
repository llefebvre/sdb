package com.datagardens.nq.sdb.client.views;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

import com.datagardens.nq.sdb.client.ClientActionAdvisor;
import com.datagardens.nq.sdb.client.ImageKey;
import com.datagardens.nq.sdb.client.SWTResourceManager;
import com.datagardens.nq.sdb.commons.model.Job;

public class JobGraphicView extends Composite 
implements ICustomComposite {

	private Image [] printer;
	private Image []computer;
	private Image [] chart;
	
	private Rectangle printerBounds;
	private Rectangle computerBounds;
	private Rectangle chartBounds;
	
	private boolean isPrinterOver;
	private boolean isComputerOver;
	private boolean isChartOver;
	
	private Job selectedJob;
//	private Form form;
	private Canvas canvas;
	private Button closeButton;
	
	public JobGraphicView(Composite parent, int style) {
		super(parent, style);
		
		printer = new Image[]{SWTResourceManager.getImage(ImageKey.PRINTER), 
				SWTResourceManager.getImage(ImageKey.PRINTER_OVER)};
		computer = new Image[]{SWTResourceManager.getImage(ImageKey.COMPUTER), 
				SWTResourceManager.getImage(ImageKey.COMPUTER_OVER)};
		chart = new Image[]{SWTResourceManager.getImage(ImageKey.GRAPH), 
				SWTResourceManager.getImage(ImageKey.GRAPH_OVER)};
		
		createContents();
	}

	@Override
	public void createContents() 
	{
		setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.verticalSpacing = 0;
		setLayout(layout);
		
		Composite area = new Composite (this, SWT.NONE);
		area.setBackground(getBackground());
		GridLayout areaLayout = new GridLayout(1, false);
		areaLayout.marginHeight=0;
		areaLayout.marginWidth=0;
		area.setLayout(areaLayout);
		area.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		canvas = new Canvas(area, SWT.NONE);
		canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		canvas.setBackground(getBackground());
		
		canvas.addPaintListener(new PaintListener() {
			
			@Override
			public void paintControl(PaintEvent e) {
				paintJob(e);
			}
		});
		
		canvas.addMouseMoveListener(new MouseMoveListener() {
			
			@Override
			public void mouseMove(MouseEvent e) 
			{
				boolean changed = false;
				
				if(printerBounds != null)
				{
					boolean oldValue = isPrinterOver;
					isPrinterOver = printerBounds.contains(new Point(e.x, e.y));
					changed = changed || oldValue != isPrinterOver;
				}
				
				if(computerBounds != null)
				{
					boolean oldValue = isComputerOver;
					isComputerOver = computerBounds.contains(new Point(e.x, e.y));
					changed = changed || oldValue != isComputerOver;
				}
				
				if(chartBounds != null)
				{
					boolean oldValue = isChartOver;
					isChartOver = chartBounds.contains(new Point(e.x, e.y));
					changed = changed || oldValue != isChartOver;
				}
				
				if(changed)
				{
					canvas.redraw();
				}
			}
		});
	}
	
	public void fillWithJob(Job job)
	{
		//repaint with job
	}
	
	private Menu createContextMenu(Control parent) 
	{
		final MenuManager menu = new MenuManager();
		menu.setRemoveAllWhenShown(true);
		menu.addMenuListener(new IMenuListener() {
			
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				
				IContributionItem items = null;
				
				if(isPrinterOver)
				{
					
				}
				
				for(IContributionItem item : ClientActionAdvisor.instnace.getProdMenu().getItems())
				{
					if(item instanceof ActionContributionItem)
					{
						ActionContributionItem actionItem = (ActionContributionItem) item;
						menu.add(actionItem.getAction());
					}
				}
			}
		});
		
		return menu.createContextMenu(parent);
	}

	public void setJob(Job job)
	{
		selectedJob = job;
//		form.setText("Job # " + job.getId());
	}

	private void paintJob(PaintEvent e) 
	{
		Rectangle drawArea = canvas.getClientArea();
		
		e.gc.setForeground(SWTResourceManager.getColor(250,250,250));
		e.gc.setBackground(SWTResourceManager.getColor(230, 230,230));
		
		
		e.gc.fillGradientRectangle(0, 0, drawArea.width, drawArea.height, true);
		
		e.gc.setBackground(SWTResourceManager.getColor(0, 148, 255));
		e.gc.setLineWidth(3);
		e.gc.setLineCap(SWT.CAP_ROUND);
		e.gc.setFont(SWTResourceManager.changeFontHeightAndMakeItBold(getFont(), 14));

		
		printerBounds = drawGraphicMenu(e.gc, printer, new Point(25, 25), 
				isPrinterOver, false, null);
		
		computerBounds = drawGraphicMenu(e.gc, computer, 
				new Point(450, (int) (printerBounds.height*.9)), 
				isComputerOver, false, new Point[]{
						new Point(printerBounds.x + printerBounds.width + 10, printerBounds.y+60), 
						new Point(450 + 60, printerBounds.y+60),
						new Point(450 + 60, ((int) (printerBounds.height*.9))-10)});
		
		int chartY = (int) (computerBounds.y + (computerBounds.height*1.2));
		
		chartBounds = drawGraphicMenu(e.gc, chart, 
				new Point(200, chartY), 
				isChartOver, false, new Point[]{
			new Point(computerBounds.x + 60, computerBounds.y + computerBounds.height + 30),
			new Point(computerBounds.x + 60, chartY + computerBounds.height - 60),
			new Point(200 + computerBounds.width + 30, chartY +computerBounds.height - 60)
		});
	}
	
	private Rectangle drawGraphicMenu(GC gc, 
			Image [] images, Point loc, boolean hover, 
			boolean clicked, Point [] linesConnectionPoints)
	{
		
		if(linesConnectionPoints != null)
		{
			//Connect to a previous graphic menu item
			//This line has to be drawn first so it says
			//at the bottom of the image
			drawArrowLines(gc, linesConnectionPoints);
		}
		
		Image imagetoPrint = images[hover?1:0];
		gc.drawImage(imagetoPrint, loc.x, loc.y);
		
		if(clicked)
		{
			//draw click menu
		}
		
		return new Rectangle(loc.x, loc.y, 
				imagetoPrint.getBounds().width, imagetoPrint.getBounds().height);
	}
	
	/*private void drawText(GC gc, String text, int color, Point loc)
	{
		Color oldColor = gc.getForeground();
		Color fontColor = SWTResourceManager.getColor(color);
		gc.setForeground(fontColor);
		gc.drawText(text, loc.x, loc.y, true);
		gc.setForeground(oldColor);
	}*/

	private void drawArrowLines(GC gc, Point[] points) 
	{
		Color oldColor = gc.getForeground();
		gc.setForeground(SWTResourceManager.getColor(0, 148, 255));
		
		if(points.length < 2)
		{
			return ;
		}
		
		
		for(int i = 0; i<points.length; i++)
		{
			Point a = points[i];
			Point b = points.length >= (i+2) ? points[i+1] : null;
			
			if(b == null)
			{
				int arrowDirection = 0;
				
				Point prev = points[i-1];
				
				if(a.x == prev.x)
				{
					//up or down
					arrowDirection = (a.y - prev.y) > 0? SWT.DOWN : SWT.UP;
				}
				else
				{
					//left of right
					arrowDirection = (a.x - prev.x) > 0? SWT.RIGHT : SWT.LEFT;
				}
				
				switch(arrowDirection)
				{
				case SWT.UP:
					gc.fillPolygon(new int[]{a.x-4, a.y, a.x, a.y-8, a.x+4, a.y});
					break;
					
				case SWT.RIGHT:
					gc.fillPolygon(new int[]{a.x, a.y-4, a.x+8, a.y, a.x, a.y+4});
					break;
					
				case SWT.DOWN:
					gc.fillPolygon(new int[]{a.x-4, a.y, a.x, a.y+8, a.x+4, a.y});
					break;
				
				case SWT.LEFT:
					gc.fillPolygon(new int[]{a.x, a.y-4, a.x-8, a.y, a.x, a.y+4});
					break;
					
				default:
					break;
				
				}
				
			}
			else
			{
				gc.drawLine(a.x, a.y, b.x, b.y);
			}
		}
		
		gc.setForeground(oldColor);
	}
}
