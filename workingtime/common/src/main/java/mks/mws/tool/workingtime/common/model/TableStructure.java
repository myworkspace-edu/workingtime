package mks.mws.tool.workingtime.common.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class TableStructure {
	int[] colWidths;
	String []colHeaders;
	List<Object[]> data;
}

