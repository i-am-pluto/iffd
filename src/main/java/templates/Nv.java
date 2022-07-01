package templates;

import javax.swing.*;

public class Nv {
        private Integer value;
        private JSpinner i;
        public Nv(JSpinner i, Integer parseInt) {
            value = parseInt;
            this.i = i;
        }

    public Nv() {

    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public JSpinner getI() {
        return i;
    }

    public void setI(JSpinner i) {
        this.i = i;
    }
}
