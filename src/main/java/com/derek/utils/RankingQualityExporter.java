package com.derek.utils;

import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

import java.io.File;
import java.text.DecimalFormat;
import java.util.*;

public class RankingQualityExporter {


    public static void main(String[] args){
        new RankingQualityExporter();
    }

    public RankingQualityExporter(){
        engine("PropertyMatrixStructure:", "Compatibility");
        engine("PropertyMatrixStructure:", "Functional_Suitability");
        engine("PropertyMatrixStructure:", "Maintainability");
        engine("PropertyMatrixStructure:", "Performance_Efficiency");
        engine("PropertyMatrixStructure:", "Portability");
        engine("PropertyMatrixStructure:", "Reliability");
        engine("PropertyMatrixStructure:", "Security");
        engine("PropertyMatrixStructure:", "Usability");
        engine("CharacteristicMatrixStructure:", "TQI");
    }

    public void engine(String headerStructure, String model){
        List<String> matrixHeaderOrder = parseInputForStructure(headerStructure);
        Map<String, Integer> maintainabilityRanks = parseInputFile(model);
        Table<String, String, String> maintainabilityTable = getTableFromRanks(matrixHeaderOrder, maintainabilityRanks);
        String maintainabilityMatrixFormatted = formatTableToMatrix(model, matrixHeaderOrder, maintainabilityTable);
        System.out.println(maintainabilityMatrixFormatted);
    }


    private String formatTableToMatrix(String title, List<String> headerOrder, Table<String, String, String> table){
        StringBuilder sb = new StringBuilder();
        String delim = "\t";
        sb.append(title + delim);
        //give header
        for (String header : headerOrder){
            sb.append(header + delim);
        }
        sb.append("\n");

        for (int colHeadCounter = 0; colHeadCounter < headerOrder.size(); colHeadCounter++) {
            sb.append(headerOrder.get(colHeadCounter) + delim);
            for (String rowValue : headerOrder){
                sb.append(table.get(rowValue, headerOrder.get(colHeadCounter)) + delim);
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    private Table<String, String, String> getTableFromRanks(List<String> headerOrder, Map<String, Integer> rankings){
        Table<String, String, String> qualityTable = TreeBasedTable.create();

        for (String row : headerOrder){
            for (String col : headerOrder){
                if (row.equals(col)){
                    //diagonal
                    qualityTable.put(row, col,"-");
                }else {
                    int rowRank = rankings.get(row);
                    int colRank = rankings.get(col);

                    int currentRowSpot = headerOrder.indexOf(row);
                    int currentColSpot = headerOrder.indexOf(col);

                    String placedValue = "-";
                    if (currentRowSpot > currentColSpot) {
                        placedValue = new DecimalFormat("#.#####").format(((double) colRank) / rowRank) + "";
                    }
                    qualityTable.put(row, col, placedValue);
                }
            }
        }

        return qualityTable;
    }

    private Map<String, Integer> parseInputFile(String qualityCharacteristic){
        Map<String, Integer> orderedQualityRankings = new HashMap<>();
        try{
            Scanner in = new Scanner(new File("configs/RawQualityRankings.txt"));

            while (in.hasNext()){
                if (in.next().equals(qualityCharacteristic + ":")){
                    String line = in.nextLine();
                    String[] lineSplitter = line.split(" ");
                    int orderedCounter = 1;
                    for (int i = 0; i < lineSplitter.length; i++){
                        String current = lineSplitter[i];
                        if (current.equals("<") || current.equals("=")) {
                            //standard
                            orderedQualityRankings.put(lineSplitter[i - 1].trim(), orderedCounter);
                        }
                        if (current.equals("<")) {
                            orderedCounter++;
                        }
                    }
                    //at very end I need to add largest one
                    orderedQualityRankings.put(lineSplitter[lineSplitter.length-1].trim(), orderedCounter);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return orderedQualityRankings;
    }
    private List<String> parseInputForStructure(String layerName){
        List<String> order = new ArrayList<>();
        try{
            Scanner in = new Scanner(new File("configs/RawQualityRankings.txt"));

            while (in.hasNext()){
                if (in.next().equals(layerName)){
                    String line = in.nextLine();
                    String[] lineSplitter = line.split("\t");
                    for (String s : lineSplitter){
                        order.add(s.trim());
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return order;
    }




}
