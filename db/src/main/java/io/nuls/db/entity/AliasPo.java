package io.nuls.db.entity;

public class AliasPo {

    private String alias;

    private String address;

    // 0: locked   1:confirm
    private int status;
    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column alias.alias
     *
     * @return the value of alias.alias
     *
     * @mbggenerated Wed Dec 13 16:27:33 CST 2017
     */
    public String getAlias() {
        return alias;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column alias.alias
     *
     * @param alias the value for alias.alias
     *
     * @mbggenerated Wed Dec 13 16:27:33 CST 2017
     */
    public void setAlias(String alias) {
        this.alias = alias == null ? null : alias.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column alias.address
     *
     * @return the value of alias.address
     *
     * @mbggenerated Wed Dec 13 16:27:33 CST 2017
     */
    public String getAddress() {
        return address;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column alias.address
     *
     * @param address the value for alias.address
     *
     * @mbggenerated Wed Dec 13 16:27:33 CST 2017
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}