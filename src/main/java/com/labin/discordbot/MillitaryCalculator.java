package com.labin.discordbot;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;

public class MillitaryCalculator {
	private Calendar calendar = new GregorianCalendar(Locale.KOREA);
	private Calendar today = Calendar.getInstance(); 
	private HashMap<String, milliData> milliMapper = new HashMap();
	private HashMap<String, String> nicknameMapper = new HashMap();
	private String name = "N/A";
	private final int SOLDIER = 640;
	private final int AIRFORCE = 730;
	private final int PUBLICSOLDIER = 730;
	private final int TECHNICALSOLDIER =1037;
	
	public MillitaryCalculator () {
		
		nicknameMapper.put("치우", "강치우");
		nicknameMapper.put("강치우", "강치우");
		nicknameMapper.put("나세", "강치우");
		
		nicknameMapper.put("구현", "권구현");
		nicknameMapper.put("권구현", "권구현");
		nicknameMapper.put("폴오리", "권구현");
		
		nicknameMapper.put("성환", "김성환");
		nicknameMapper.put("김성환", "김성환");
		nicknameMapper.put("리릭", "김성환");


		nicknameMapper.put("욱현", "김욱현");
		nicknameMapper.put("김욱현", "김욱현");
		nicknameMapper.put("쌍부랄의사나이", "김욱현");
		
		nicknameMapper.put("용진", "김용진");
		nicknameMapper.put("김용진", "김용진");
		nicknameMapper.put("용진로제", "김용진");

		nicknameMapper.put("동훈", "우동훈");
		nicknameMapper.put("우동훈", "우동훈");
		nicknameMapper.put("씨발련아", "우동훈");

		nicknameMapper.put("동민", "이동민");
		nicknameMapper.put("이동민", "이동민");

		nicknameMapper.put("수현", "이수현");
		nicknameMapper.put("이수현", "이수현");
		nicknameMapper.put("라빈", "이수현");
		
		nicknameMapper.put("현규", "이현규");
		nicknameMapper.put("이현규", "이현규");
		nicknameMapper.put("망자", "이현규");

		nicknameMapper.put("태열", "최태열");
		nicknameMapper.put("최태열", "최태열");
		nicknameMapper.put("빨리죽기", "최태열");

		nicknameMapper.put("정우", "최정우");
		nicknameMapper.put("최정우", "최정우");
		nicknameMapper.put("목검", "최정우");
		
		milliMapper.put("강치우",new milliData("2017.4.3","2019.11.11"));
		milliMapper.put("권구현",new milliData("2017.6.26",PUBLICSOLDIER));
		milliMapper.put("김성환",new milliData("2017.4.3",SOLDIER));
		milliMapper.put("김욱현",new milliData("2017.2.13",AIRFORCE));
		milliMapper.put("우동훈",new milliData("2016.8.2",SOLDIER));
		milliMapper.put("이동민",new milliData("2017.2.13",SOLDIER));
		milliMapper.put("이수현",new milliData("2018.3.9",TECHNICALSOLDIER));
		milliMapper.put("이현규",new milliData("2017.5.15",SOLDIER));
		milliMapper.put("최태열",new milliData("2018.2.12",SOLDIER));
	
		
		milliMapper.put("김용진",new milliData("0.0.0","9999.9.9"));
		milliMapper.put("최정우",new milliData("0.0.0","9999.9.9"));
	}
	
	public void setName(String user) {
		name=user;
	}
	
	public String getStartDate() {
		return milliMapper.get(nicknameMapper.get(name)).getStartDate();
	}
	
	public String getEndDate() {
		return milliMapper.get(nicknameMapper.get(name)).getEndDate();
		
	}
	
	public int getStartDays() {
        today.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH) + 1,calendar.get(Calendar.DAY_OF_MONTH)); 
        return milliMapper.get(nicknameMapper.get(name)).getStartDays(today);
	}
	
	public int getEndDays() {
        today.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH) + 1,calendar.get(Calendar.DAY_OF_MONTH)); 
        return milliMapper.get(nicknameMapper.get(name)).getEndDays(today);
	}
	
	public double getStartPercent() {
        today.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH) + 1,calendar.get(Calendar.DAY_OF_MONTH)); 
        return milliMapper.get(nicknameMapper.get(name)).getStartPercent(today);
	}
	public double getEndPercent() {
        today.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH) + 1,calendar.get(Calendar.DAY_OF_MONTH)); 
        return milliMapper.get(nicknameMapper.get(name)).getEndPercent(today);
	}
	
	public String getRealName() {
		return nicknameMapper.get(name);
	}
	
	public String broadcastNickName() {
		String returnkey="";
		Set key = nicknameMapper.keySet();
		  
		for (Iterator iterator = key.iterator(); iterator.hasNext();) {
			String keyName = (String) iterator.next();
			returnkey+=keyName+", ";
			}
		returnkey.substring(0,returnkey.length()-2);
		return returnkey;
	}
		  
}

class milliData {
	private Calendar startDate = Calendar.getInstance();
	private Calendar endDate = Calendar.getInstance();
	private int AllDay=0;
	
	public milliData(String startday, String endday)
	{
		StringTokenizer st;
		st = new StringTokenizer(startday,".");
		startDate.set(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
		st = new StringTokenizer(endday,".");
		endDate.set(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
		
		AllDay = calcDateforDay(startDate, endDate);
	}
	
	public milliData(String startday, int service)
	{
		StringTokenizer st;
		st = new StringTokenizer(startday,".");
		startDate.set(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
		st = new StringTokenizer(startday,".");
		endDate.set(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
		endDate.add(Calendar.DAY_OF_YEAR, service);
		
		AllDay = calcDateforDay(startDate, endDate);
	}
	
	public String getStartDate() {
		return startDate.get(Calendar.YEAR)+"."+startDate.get(Calendar.MONTH)+"."+startDate.get(Calendar.DAY_OF_MONTH);
	}

	public String getEndDate() {
		return endDate.get(Calendar.YEAR)+"."+endDate.get(Calendar.MONTH)+"."+endDate.get(Calendar.DAY_OF_MONTH);
	}
	
	public int getStartDays(Calendar today) {
		return calcDateforDay(startDate,today);
	}
	
	public int getEndDays(Calendar today) {
		return calcDateforDay(today,endDate);
	}
	
	public double getStartPercent(Calendar today) {
		return (double)getStartDays(today)/ (double)AllDay* (double)100;
	}
	
	public double getEndPercent(Calendar today) {
		return (double)getEndDays(today)/ (double)AllDay* (double)100;
	}
	
	public int calcDateforDay(Calendar start,Calendar end) {
		return (int)((end.getTimeInMillis()-start.getTimeInMillis())/(1000*60*60*24));
	}
		
}