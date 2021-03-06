package io.nuls.core.chain.manager;

import io.nuls.core.validate.NulsDataValidator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Niels
 * @date 2017/12/7
 */
public class TransactionValidatorManager {

    private static final List<NulsDataValidator> ALL_LIST = new ArrayList<>();

    /**
     * the validator fit all transaction instances
     * @param validator
     */
    public static void addTxDefValidator(NulsDataValidator validator) {
        ALL_LIST.add(validator);
    }

    public static final List<NulsDataValidator> getValidators() {
        List<NulsDataValidator> list = new ArrayList<>();
        list.addAll(ALL_LIST);
        return list;
    }
}
