package com.worf.hadoop.config;

public class MrJobConfig extends AbstractConfig {

	private static final long serialVersionUID = -6783827216925347352L;

	private String classname;
	
	private String inputpath;
	
	private String outputpath;
	
	private ChainMapperConfig chainMapper;
	
	private ChainReducerConfig chainReducer;
	
	private MapperConfig mapper;
	
	private ReducerConfig reducer;
	
	private CombinerConfig combiner;
	
	private PartitionerConfig partitioner;
	
	private GroupingComparatorConfig comparator;
	
	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public ChainMapperConfig getChainMapper() {
		return chainMapper;
	}

	public void setChainMapper(ChainMapperConfig chainMapper) {
		this.chainMapper = chainMapper;
	}

	public ChainReducerConfig getChainReducer() {
		return chainReducer;
	}

	public void setChainReducer(ChainReducerConfig chainReducer) {
		this.chainReducer = chainReducer;
	}

	public MapperConfig getMapper() {
		return mapper;
	}

	public void setMapper(MapperConfig mapper) {
		this.mapper = mapper;
	}

	public ReducerConfig getReducer() {
		return reducer;
	}

	public void setReducer(ReducerConfig reducer) {
		this.reducer = reducer;
	}

	public CombinerConfig getCombiner() {
		return combiner;
	}

	public void setCombiner(CombinerConfig combiner) {
		this.combiner = combiner;
	}

	public PartitionerConfig getPartitioner() {
		return partitioner;
	}

	public void setPartitioner(PartitionerConfig partitioner) {
		this.partitioner = partitioner;
	}

	public GroupingComparatorConfig getComparator() {
		return comparator;
	}

	public void setComparator(GroupingComparatorConfig comparator) {
		this.comparator = comparator;
	}

	public String getInputpath() {
		return inputpath;
	}

	public void setInputpath(String inputpath) {
		this.inputpath = inputpath;
	}

	public String getOutputpath() {
		return outputpath;
	}

	public void setOutputpath(String outputpath) {
		this.outputpath = outputpath;
	}

	@Override
	public String toString() {
		return "MrJobConfig [classname=" + classname + ", inputpath="
				+ inputpath + ", outputpath=" + outputpath + ", chainMapper="
				+ chainMapper + ", chainReducer=" + chainReducer + ", mapper="
				+ mapper + ", reducer=" + reducer + ", combiner=" + combiner
				+ ", partitioner=" + partitioner + ", comparator=" + comparator
				+ "]";
	}
	
}
