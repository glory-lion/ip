package lion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

/**
 * Tests task-list operations that change task order or completion state.
 */
public class TaskListTest {

    @Test
    void add_multipleTasks_increasesSizeAndPreservesOrder() {
        TaskList tasks = new TaskList();
        Task first = new Todo("read book");
        Task second = new Todo("write notes");

        tasks.add(first);
        tasks.add(second);

        assertEquals(2, tasks.size());
        assertSame(first, tasks.get(0));
        assertSame(second, tasks.get(1));
    }

    @Test
    void delete_middleTask_returnsDeletedTaskAndClosesGap() {
        TaskList tasks = new TaskList();
        Task first = new Todo("first");
        Task middle = new Todo("middle");
        Task last = new Todo("last");
        tasks.add(first);
        tasks.add(middle);
        tasks.add(last);

        Task deleted = tasks.delete(1);

        assertSame(middle, deleted);
        assertEquals(2, tasks.size());
        assertSame(first, tasks.get(0));
        assertSame(last, tasks.get(1));
    }

    @Test
    void markThenUnmark_taskStatusChangesAccordingly() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("read book"));

        tasks.mark(0);
        assertEquals("1", tasks.get(0).getStatusIcon());

        tasks.unmark(0);
        assertEquals("0", tasks.get(0).getStatusIcon());
    }
}
