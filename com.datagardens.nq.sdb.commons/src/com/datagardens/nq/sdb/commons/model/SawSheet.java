package com.datagardens.nq.sdb.commons.model;

import java.util.HashMap;
import java.util.Map;

public class SawSheet extends NQModelObject
{
    public static final int TOTAL_NUM_OF_CUTS = 30;
    
    private String jobId;
    
	private Map<Integer, IndividualCut> cuts;
    private int sawSheetNumber;
    private int quantityBlanks;
    private int blankLength;
    private int yieldPerBlank;
    private int plannedNetYield;
    private int totalActualCutYield;
    
    private int numberOfShifts;
    private int operationNumber;
    
    
    public static SawSheet parse(String jobId, String sawSheetNumber, String quantityBlanks, 
    		String blankLength, String yieldPerBlank, String plannedNetYield, String totalActualCutYield, String numberOfShifts)
    {
    	quantityBlanks = quantityBlanks == null || quantityBlanks.isEmpty() ? "0" : quantityBlanks;
    	blankLength    = blankLength == null || blankLength.isEmpty() ? "0" : blankLength;
    	yieldPerBlank  = yieldPerBlank == null || yieldPerBlank.isEmpty() ? "0" : yieldPerBlank;
    	plannedNetYield = plannedNetYield == null || plannedNetYield.isEmpty() ? "0" : plannedNetYield;
    	totalActualCutYield    = totalActualCutYield == null || totalActualCutYield.isEmpty() ? "0" : totalActualCutYield;
    	numberOfShifts = numberOfShifts == null || numberOfShifts.isEmpty() ? "1" : numberOfShifts;
    	
    	return new SawSheet(jobId,
    			Integer.parseInt(sawSheetNumber),
    			Integer.parseInt(quantityBlanks), 
    			Integer.parseInt(blankLength), 
    			Integer.parseInt(yieldPerBlank), 
    			Integer.parseInt(plannedNetYield),
    			Integer.parseInt(totalActualCutYield), 
    			Integer.parseInt(numberOfShifts));
    }
    
    private SawSheet(String jobId, int sawSheetNumber, int quantityBlanks, 
    		int blankLength, int yieldPerBlank, int plannedNetYield, int totalActualCutYield, int numberOfShifts)
    {
    	super("");
    	this.jobId = jobId;
        this.sawSheetNumber = sawSheetNumber;
        this.quantityBlanks = quantityBlanks;
        this.blankLength = blankLength;
        this.yieldPerBlank = yieldPerBlank;
        this.plannedNetYield = plannedNetYield;
        this.totalActualCutYield = totalActualCutYield;
        this.numberOfShifts = numberOfShifts;
        this.operationNumber = 10;
        
        cuts = new HashMap<Integer, IndividualCut>();
    }   

	@Override
	public String toString() {
		return "SawSheet [jobId=" + jobId + ", cuts=" + cuts
				+ ", sawSheetNumber=" + sawSheetNumber + ", quantityBlanks="
				+ quantityBlanks + ", blankLength=" + blankLength
				+ ", yieldPerBlank=" + yieldPerBlank + ", plannedNetYield="
				+ plannedNetYield + ", totalActualCutYield="
				+ totalActualCutYield + ", numberOfShifts=" + numberOfShifts
				+ ", operationNumber=" + operationNumber + "]";
	}

	public void addCut(IndividualCut cut)
    {
        cuts.put(cut.getIndividualCutNumber(), cut);
    }

    public void modifyCut(IndividualCut cut)
    {
        IndividualCut curretCut = cuts.get(cut.getIndividualCutNumber());
        if(curretCut != null)
        {
            curretCut.merge(cut);
        }
        else
        {
            addCut(cut);
        }
    }

    public Map<Integer, IndividualCut> getCuts() {
        return cuts;
    }

    public String getJobId() {
		return jobId;
	}
    
    public Integer getSawSheetNumber(){
        return sawSheetNumber;
    }
    
    public int getQuantityBlanks() {
		return quantityBlanks;
	}
    
    public int getBlankLength() {
		return blankLength;
	}
    
    public int getYieldPerBlank() {
		return yieldPerBlank;
	}
    
    public int getPlannedNetYield() {
		return plannedNetYield;
	}
    
    public int getTotalActualCutYield() {
		return totalActualCutYield;
	}
    
	public void setSawSheetNumber(int sawSheetNumber) {
		this.sawSheetNumber = sawSheetNumber;
	}

	public void setQuantityBlanks(int quantityBlanks) {
		this.quantityBlanks = quantityBlanks;
	}

	public void setBlankLength(int blankLength) {
		this.blankLength = blankLength;
	}

	public void setYieldPerBlank(int yieldPerBlank) {
		this.yieldPerBlank = yieldPerBlank;
	}

	public void setPlannedNetYield(int plannedNetYield) {
		this.plannedNetYield = plannedNetYield;
	}

	public void setTotalActualCutYield(int totalActualCutYield) {
		this.totalActualCutYield = totalActualCutYield;
	}

	public int getNumberOfShifts() {
		return numberOfShifts;
	}
	
	public int addShift()
	{
		numberOfShifts++;
		return numberOfShifts;
	}
	
	public int removeShift()
	{
		numberOfShifts = numberOfShifts-1 < 1 ? 1 : numberOfShifts -1;
		return numberOfShifts;
	}
	
	public int getOperationNumber() {
		return operationNumber;
	}
	
	public void merge(SawSheet sawSheetFromServer) 
	{
		quantityBlanks = sawSheetFromServer.getQuantityBlanks();
		blankLength    = sawSheetFromServer.getBlankLength();
		yieldPerBlank  = sawSheetFromServer.getYieldPerBlank();
		
		for(IndividualCut cut : sawSheetFromServer.getCuts().values())
		{
			modifyCut(cut);
		}
	}
}


