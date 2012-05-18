package com.datagardens.nq.sdb.client;

public interface ImageKey 
{
	final String IMAGES_PATH = "/images/";
	final String ICONS_PATH = IMAGES_PATH + "icons/";
	final String ICONS_32_PATH = ICONS_PATH + "32/";
	final String TITLES_PATH = IMAGES_PATH + "titles/";
			
	public final String ICON_TRACKING =  ICONS_PATH + "package.png";
	public final String iCON_PRODUCTION = ICONS_PATH + "factory.png";
	public final String ICON_REPORTING = ICONS_PATH + "chart_line.png";
	public final String ICON_ADMINSTRATION = ICONS_PATH + "administrator.png";
	
	public final String ICON_USER = ICONS_PATH + "user.png";
	public final String ICON_USER_ADD = ICONS_PATH + "user_add.png";
	public final String ICON_USER_EDIT = ICONS_PATH + "user_edit.png";
	public final String ICON_USER_DELETE = ICONS_PATH + "user_delete.png";
	
	
	public final String ICON_PRODUCTION_JOB = ICONS_PATH + "gear_in.png";
	public final String ICON_PRODUCTION_PRINT = ICONS_PATH + "printer.png";
	public final String ICON_PRODUCTION_DATA_ENTRY = ICONS_PATH + "data_chooser.png";
	public final String ICON_PRODUCTION_ANALYSIS = ICONS_PATH + "zoom_extend.png";
	public final String ICON_SAW_SHEET = ICONS_PATH + "page_gear.png";
	public final String ICON_SHIFT_LOG = ICONS_PATH + "document_copies.png";
	public final String ICON_OPERATIONS = ICONS_PATH + "cog.png";
	public final String ICON_SHIFT = ICONS_PATH + "clock.png";
	public final String ICON_SHIFT_ADD = ICONS_PATH + "time_add.png";
	public final String ICON_SHIFT_DELETE = ICONS_PATH + "time_delete.png";

	
	public final String ICON_OPERATION = ICONS_PATH + "brick.png";
	public final String ICON_CLOSE = ICONS_PATH + "cross.png";
	public final String ICON_SAVE = ICONS_PATH + "disk.png";
	
	/*32x32*/
	public final String ICON_32_PRODUCTION_PRINT = ICONS_32_PATH + "printer.png";
	public final String ICON_32_ADMINISTRATOR = ICONS_32_PATH + "administrator.png";
	
	/* titles */
	public final String TITLE_JOB = TITLES_PATH + "gear.png";
	public final String TITLE_USER_ADD = TITLES_PATH + "user_add.png";
	
	public final String WELCOME = IMAGES_PATH + "welcome.png";
	public final String LOGO = IMAGES_PATH + "logo.png";
	public final String PRINTER = IMAGES_PATH + "print.png";
	public final String COMPUTER = IMAGES_PATH + "data_entry.png";
	public final String GRAPH = IMAGES_PATH + "analysis.png";
	public final String PRINTER_OVER = IMAGES_PATH + "print_over.PNG";
	public final String COMPUTER_OVER = IMAGES_PATH + "data_entry_over.PNG";
	public final String GRAPH_OVER = IMAGES_PATH + "analysis_over.PNG";
}
