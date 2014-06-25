import java.net.*;
import java.util.*;
import java.util.regex.*;
import org.json.*;

public class TocHw4 {

	public static void main(String[] args)throws Exception {
		
		if(args.length<1){
			System.out.println("參數錯誤");
			return;
		}
		JSONArray input = null;
		JSONObject json;
		String street;
		int streetIndex;
		int tradeDate;
		int trade;
		int maxDis=0;
		ArrayList<String> streets=new ArrayList<String>();
		ArrayList<Integer>  maxTrade=new ArrayList<Integer>();
		ArrayList<Integer>  minTrade=new ArrayList<Integer>();
		ArrayList<Integer>  output=new ArrayList<Integer>();
		ArrayList<ArrayList<Integer>> disMonth=new ArrayList<ArrayList<Integer>>();
		int streetEnd;
		try {
			input=getJSONFromURL(args[0]);
		} catch (Exception e) {
		}
		int length=input.length();
		
		for(int a=0;a<length;a++){
			json= new JSONObject(new JSONTokener(input.get(a).toString()));
			street=json.getString("土地區段位置或建物區門牌");
			if((streetEnd=indexOf("路([0-9]|(.段))", street))!=-1){
				street=street.substring(0, streetEnd+1);
			}else if((streetEnd=indexOf("街([0-9]|(.段))", street))!=-1){
				street=street.substring(0, streetEnd+1);
			}else if((streetEnd=indexOf("大道([0-9]|(.段))", street))!=-1){
				street=street.substring(0, streetEnd+2);
			}else if((streetEnd=indexOf("巷([0-9]|(.段))", street))!=-1){
				street=street.substring(0, streetEnd+1);
			}else{
				continue;
			}
			streetIndex=streets.indexOf(street);
			tradeDate=json.getInt("交易年月");
			trade=json.getInt("總價元");
			ArrayList<Integer> tempList;
			if(streetIndex==-1){
				tempList=new ArrayList<Integer>();
				tempList.add(tradeDate);
				maxTrade.add(new Integer(trade));
				minTrade.add(new Integer(trade));
				disMonth.add(tempList);
				streets.add(street.toString());
			}else{
				if(trade>maxTrade.get(streetIndex)){
					maxTrade.set(streetIndex, trade);
				}else if(trade<minTrade.get(streetIndex)){
					minTrade.set(streetIndex, trade);
				}
				if(disMonth.get(streetIndex).indexOf(tradeDate)==-1){
					disMonth.get(streetIndex).add(tradeDate);
				}
			}
		}
		int streetsSize=streets.size();
		int dis;
		for(int a=0;a<streetsSize;a++){
			dis=disMonth.get(a).size();
			if(dis>maxDis){
				maxDis=dis;
				output.clear();
				output.add(a);
			}else if(dis==maxDis){
				output.add(a);
			}
		}
		for(int a:output){
			System.out.println(streets.get(a)+", 最高成交價: "+maxTrade.get(a)+", 最低成交價: "+minTrade.get(a));
		}
		System.out.println();
	}
	
	public static JSONArray getJSONFromURL(String url) throws Exception{
		URL website = new URL(url);
        URLConnection connection = website.openConnection();
		JSONArray json=new JSONArray(new JSONTokener(connection.getInputStream()));
		return json;
    }
	
	public static int indexOf(String pattern, String s) {
	    Matcher matcher = Pattern.compile(pattern).matcher(s);
	    return matcher.find() ? matcher.start() : -1;
	}
}
