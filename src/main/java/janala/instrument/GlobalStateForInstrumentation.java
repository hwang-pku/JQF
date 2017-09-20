package janala.instrument;

/** An object to keep track of (classId, methodId, instructionId) tuples during
 instrumentation. */
public class GlobalStateForInstrumentation {
  public static GlobalStateForInstrumentation instance = new GlobalStateForInstrumentation();
  private int iid = 0;
  private int mid = 0;
  private int cid = 0;

  // When one gets the id, she gets the result of merging all three ids.
  // NOTE: Beaware of truncation errors.
  private final static int CBITS = 10; // CID occupies the upper 10 bits
  private final static int MBITS = 8;  // MID occupies the next 8 bits
  private final static int IBITS = 32 - CBITS - MBITS;  // MID occupies the next 8 bits

  /** Increment iid and get the complete id */
  public int incAndGetId() {
    iid++;
    validate(iid, (32 - CBITS - MBITS));
    return getId();
  }

  public int getId() {
    return (cid << (32 - CBITS)) + (mid << (32 - CBITS - MBITS)) + iid;
  }

  public int getMid() {
    return mid;
  }

  public void incMid() {
    this.mid++;
    validate(mid, MBITS);
    this.iid = 0;
  }

  public int getCid() {
    return cid;
  }

  public void setCid(int cid) {
    cid = Math.abs(cid) % CBITS;
    validate(cid, CBITS);
    this.iid = 0;
    this.mid = 0;
    this.cid = cid;
  }

  private void validate(int id, int bits) {
    if (id < 0 || id >= (1 << bits)) {
      throw new IllegalArgumentException("Invalid instruction ID range");
    }
  }
}
