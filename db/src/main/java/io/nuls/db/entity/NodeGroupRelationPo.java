package io.nuls.db.entity;

/**
 * @author Niels
 * @date 2017/11/20
 */
public class NodeGroupRelationPo {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column node_group_relation.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column node_group_relation.node_id
     *
     * @mbg.generated
     */
    private Integer nodeId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column node_group_relation.group_id
     *
     * @mbg.generated
     */
    private Integer groupId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column node_group_relation.id
     *
     * @return the value of node_group_relation.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column node_group_relation.id
     *
     * @param id the value for node_group_relation.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column node_group_relation.node_id
     *
     * @return the value of node_group_relation.node_id
     *
     * @mbg.generated
     */
    public Integer getNodeId() {
        return nodeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column node_group_relation.node_id
     *
     * @param nodeId the value for node_group_relation.node_id
     *
     * @mbg.generated
     */
    public void setNodeId(Integer nodeId) {
        this.nodeId = nodeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column node_group_relation.group_id
     *
     * @return the value of node_group_relation.group_id
     *
     * @mbg.generated
     */
    public Integer getGroupId() {
        return groupId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column node_group_relation.group_id
     *
     * @param groupId the value for node_group_relation.group_id
     *
     * @mbg.generated
     */
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
}