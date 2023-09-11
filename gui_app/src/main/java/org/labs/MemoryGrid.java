package org.labs;

import java.awt.Button;
import java.util.List;

public class MemoryGrid implements Displayable{

    private final List<MemoryCell> cells = List.of (
       new MemoryCell("mem1", 425, 20, this),
       new MemoryCell("mem2", 550, 20, this),
       new MemoryCell("mem3", 675, 20, this)
    );

    public void uncheckAll() {
        cells.forEach(MemoryCell::uncheck);
    }

    private MemoryCell getChecked() {
        return cells.stream()
                .filter(MemoryCell::isChecked)
                .findFirst()
                .orElseThrow(() ->
                    new RuntimeException("There is no checked cell"));
    }

    @Override
    public void display(MainFrame frame) {
        cells.forEach(cell ->
                cell.display(frame)
        );
        Button clearMem = new Button("MC");
        Button addCurr = new Button("M+");

        clearMem.setSize(65, 65);
        clearMem.setLocation(505, 140);

        addCurr.setSize(65, 65);
        addCurr.setLocation(630, 140);

        clearMem.addActionListener(listener -> getChecked().clear());

        addCurr.addActionListener(listener ->
            getChecked().add(Context.equation.result)
        );

        frame.add(clearMem);
        frame.add(addCurr);
    }
}
