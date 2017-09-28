package com.szzc.zookeeper.monitor.response;

public class CommonResponseParser extends ResponseParser {
	
	@Override
	protected void parseLine(String line) {
		((CommonResponse) result).add(line);
	}


}
