package com.jpa.demo.param;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author lee
 * @date 2019年1月8日
 */
@Data
@AllArgsConstructor(staticName = "of")
public class StringData {

	private String data;

	private Boolean isJson;

}
