package com.stackroute.datamunger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*There are total 5 DataMungertest files:
 * 
 * 1)DataMungerTestTask1.java file is for testing following 3 methods
 * a)getSplitStrings()  b) getFileName()  c) getBaseQuery()
 * 
 * Once you implement the above 3 methods,run DataMungerTestTask1.java
 * 
 * 2)DataMungerTestTask2.java file is for testing following 3 methods
 * a)getFields() b) getConditionsPartQuery() c) getConditions()
 * 
 * Once you implement the above 3 methods,run DataMungerTestTask2.java
 * 
 * 3)DataMungerTestTask3.java file is for testing following 2 methods
 * a)getLogicalOperators() b) getOrderByFields()
 * 
 * Once you implement the above 2 methods,run DataMungerTestTask3.java
 * 
 * 4)DataMungerTestTask4.java file is for testing following 2 methods
 * a)getGroupByFields()  b) getAggregateFunctions()
 * 
 * Once you implement the above 2 methods,run DataMungerTestTask4.java
 * 
 * Once you implement all the methods run DataMungerTest.java.This test case consist of all
 * the test cases together.
 */

public class DataMunger {

	/*
	 * This method will split the query string based on space into an array of words
	 * and display it on console
	 */

	public String[] getSplitStrings(String queryString) {
		String [] strArr = queryString.toLowerCase().split(" ");
		return strArr;
	}

	/*
	 * Extract the name of the file from the query. File name can be found after a
	 * space after "from" clause. Note: ----- CSV file can contain a field that
	 * contains from as a part of the column name. For eg: from_date,from_hrs etc.
	 * 
	 * Please consider this while extracting the file name in this method.
	 */

	public String getFileName(String queryString) {
		
		int indexOfFromClause = queryString.indexOf("from");
		int indexOfCsv = queryString.indexOf("csv");
		return queryString.substring((indexOfFromClause+5), (indexOfCsv+3));
	}

	/*
	 * This method is used to extract the baseQuery from the query string. BaseQuery
	 * contains from the beginning of the query till the where clause
	 * 
	 * Note: ------- 1. The query might not contain where clause but contain order
	 * by or group by clause 2. The query might not contain where, order by or group
	 * by clause 3. The query might not contain where, but can contain both group by
	 * and order by clause
	 */
	
	public String getBaseQuery(String queryString) {
		if(queryString.contains("where")) {
			return queryString.substring(0, (queryString.indexOf("where")-1));
		}else {
			if(queryString.contains("group by") && queryString.contains("order by")) {
				return queryString.substring(0, (queryString.indexOf("group by")-1));
			}else if(queryString.contains("group by")) {
				return queryString.substring(0, (queryString.indexOf("group by")-1));
			}else if(queryString.contains("order by")) {
				return queryString.substring(0, (queryString.indexOf("order by")-1));
			}else {
				return queryString;
			}
		}
	}

	/*
	 * This method will extract the fields to be selected from the query string. The
	 * query string can have multiple fields separated by comma. The extracted
	 * fields will be stored in a String array which is to be printed in console as
	 * well as to be returned by the method
	 * 
	 * Note: 1. The field name or value in the condition can contain keywords
	 * as a substring. For eg: from_city,job_order_no,group_no etc. 2. The field
	 * name can contain '*'
	 * 
	 */
	
	public String[] getFields(String queryString) {
		String [] str =queryString.substring((queryString.indexOf("select ")+7), (queryString.indexOf(" from"))).split(",");
		return str;
	}

	/*
	 * This method is used to extract the conditions part from the query string. The
	 * conditions part contains starting from where keyword till the next keyword,
	 * which is either group by or order by clause. In case of absence of both group
	 * by and order by clause, it will contain till the end of the query string.
	 * Note:  1. The field name or value in the condition can contain keywords
	 * as a substring. For eg: from_city,job_order_no,group_no etc. 2. The query
	 * might not contain where clause at all.
	 */
	
	public String getConditionsPartQuery(String queryString) {
		queryString = queryString.toLowerCase();
		int whereIndex = queryString.indexOf("where");
		if(whereIndex >= 0) {
		 if(queryString.contains("group by")) {
				return queryString.substring((whereIndex+6),(queryString.indexOf("group by"))).trim();
			}else if (queryString.contains("order by")){
				return queryString.substring((whereIndex+6),(queryString.indexOf("order by"))).trim();
			}else {
				return queryString.substring((whereIndex+6)).trim();
			}
		}else {
			return null;
		}
	}

	/*
	 * This method will extract condition(s) from the query string. The query can
	 * contain one or multiple conditions. In case of multiple conditions, the
	 * conditions will be separated by AND/OR keywords. for eg: Input: select
	 * city,winner,player_match from ipl.csv where season > 2014 and city
	 * ='Bangalore'
	 * 
	 * This method will return a string array ["season > 2014","city ='bangalore'"]
	 * and print the array
	 * 
	 * Note: ----- 1. The field name or value in the condition can contain keywords
	 * as a substring. For eg: from_city,job_order_no,group_no etc. 2. The query
	 * might not contain where clause at all.
	 */

	public String[] getConditions(String queryString) {
		queryString = queryString.toLowerCase();
		if(queryString.contains("where")) {
		String tmpStr = queryString.split("where ")[1];
		if(tmpStr.contains("group by")) {
			tmpStr = tmpStr.split("group by")[0];
		}else if(tmpStr.contains("order by")) {
			tmpStr = tmpStr.split("order by")[0];
		}
		String [] resArr = tmpStr.trim().split(" and | or ");
		return resArr;
			
		}else {
			return null;
		}
	}

	/*
	 * This method will extract logical operators(AND/OR) from the query string. The
	 * extracted logical operators will be stored in a String array which will be
	 * returned by the method and the same will be printed Note:  1. AND/OR
	 * keyword will exist in the query only if where conditions exists and it
	 * contains multiple conditions. 2. AND/OR can exist as a substring in the
	 * conditions as well. For eg: name='Alexander',color='Red' etc. Please consider
	 * these as well when extracting the logical operators.
	 * 
	 */

	public String[] getLogicalOperators(String queryString) {
		queryString = queryString.toLowerCase();
		String tmpStr = null;
		if(queryString.contains("where")) {
			tmpStr = queryString.split("where ")[1];
			if(tmpStr.contains("group by")) {
				tmpStr = tmpStr.split("group by")[0];
			}else if(tmpStr.contains("order by")) {
				tmpStr = tmpStr.split("order by")[0];
			}
			String [] tmpArr = tmpStr.split(" ");
			List <String> resList = new ArrayList<>(); 
			for(int i =0; i<tmpArr.length;i++) {
				if(tmpArr[i].equals("and") || tmpArr[i].equals("or")) {
					resList.add(tmpArr[i]);
				}
			}
			String [] result =  new String[resList.size()];
			for (int i = 0; i < resList.size(); i++) {
				result[i] = resList.get(i);
				
			}
			return result;
			}else {
				
				return null;
			}
	}

	/*
	 * This method extracts the order by fields from the query string. Note: 
	 * 1. The query string can contain more than one order by fields. 2. The query
	 * string might not contain order by clause at all. 3. The field names,condition
	 * values might contain "order" as a substring. For eg:order_number,job_order
	 * Consider this while extracting the order by fields
	 */

	public String[] getOrderByFields(String queryString) {
		queryString = queryString.toLowerCase();
		if(queryString.contains("order by")) {
			String tmpArr = queryString.split("order by ")[1];
			String [] res =tmpArr.split(" ");
			return res;
		}

		return null;
	}

	/*
	 * This method extracts the group by fields from the query string. Note:
	 * 1. The query string can contain more than one group by fields. 2. The query
	 * string might not contain group by clause at all. 3. The field names,condition
	 * values might contain "group" as a substring. For eg: newsgroup_name
	 * 
	 * Consider this while extracting the group by fields
	 */

	public String[] getGroupByFields(String queryString) {
		queryString = queryString.toLowerCase();
		if(queryString.contains("group by")) {
			String tmpArr = queryString.split("group by ")[1];
			String [] res =tmpArr.split(" ");
			return res;
		}

		return null;
	}

	/*
	 * This method extracts the aggregate functions from the query string. Note:
	 *  1. aggregate functions will start with "sum"/"count"/"min"/"max"/"avg"
	 * followed by "(" 2. The field names might
	 * contain"sum"/"count"/"min"/"max"/"avg" as a substring. For eg:
	 * account_number,consumed_qty,nominee_name
	 * 
	 * Consider this while extracting the aggregate functions
	 */

	public String[] getAggregateFunctions(String queryString) {
		queryString = queryString.toLowerCase();
		String val = queryString.split("from")[0];
		String tmpVal = val.split(" ")[1];
		String [] arr = tmpVal.split(",");
		List <String> myList = new ArrayList<String>();
		for (int i = 0; i < arr.length; i++) {
			if((arr[i].contains("count") || arr[i].contains("min") || arr[i].contains("max")
					|| arr[i].contains("avg") || arr[i].contains("sum"))) {
				myList.add(arr[i]);
			}
		}
		String [] resArr = new String[myList.size()];
		if(myList.size()> 0) {
			for (int i = 0; i < myList.size(); i++) {
				resArr[i] = myList.get(i);
			}
		}else {
			return null;
		}
		
		return resArr;
	}

}