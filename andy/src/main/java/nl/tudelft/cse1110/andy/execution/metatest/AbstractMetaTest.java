package nl.tudelft.cse1110.andy.execution.metatest;

import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.config.MetaTest;
import nl.tudelft.cse1110.andy.config.RunConfiguration;
import nl.tudelft.cse1110.andy.execution.Context;

public abstract class AbstractMetaTest implements MetaTest {

    private final int weight;
    private final String name;

    protected AbstractMetaTest(int weight, String name) {
        this.weight = weight;
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public String getNameAndWeight() {
        return String.format("%s (weight: %d)", name, weight);
    }

    @Override
    public abstract boolean execute(Context ctx, DirectoryConfiguration dirCfg, RunConfiguration runCfg)
            throws Exception;

}
