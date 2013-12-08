package parser.objects.plan;

/**
 * Access strategies PostgreSQL uses to access or unite relations. Taken from <a
 * href="http://doxygen.postgresql.org/allpaths_8c_source.html">the source</a>
 * (on the day of 13/12/07)
 * 
 * @author Daniel
 * 
 */
public enum AccessStrategy {
	IdxScan, SeqScan, HashJoin, NestLoop, Material, BitmapHeapScan, BitmapAndPath, BitmapOrPath, TidScan, ForeignScan, Append, MergeAppend, Result, Unique, MergeJoin
	// ???Path
}
