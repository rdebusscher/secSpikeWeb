package org.secspike.todo.reminders.boundary;

import org.secspike.todo.reminders.control.AccessGuard;
import org.secspike.todo.reminders.entity.ToDo;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author airhacks.com
 */
@Stateless
public class ToDoManager {

    @PersistenceContext
    EntityManager em;

    @Inject
    AccessGuard accessGuard;

    public ToDo update(ToDo toDo) {
        String userName = this.accessGuard.getUserName();
        if (!toDo.isNew() && find(toDo.getId()) != null) {
            toDo.setChangedBy(userName);
        } else {
            toDo.setCreatedBy(userName);
        }
        return em.merge(toDo);
    }

    public void delete(long id) {
        ToDo toDo = this.em.getReference(ToDo.class, id);
        this.em.remove(toDo);
    }

    public ToDo find(long id) {
        return this.em.find(ToDo.class, id);
    }

    public List<ToDo> all() {
        return this.em.createNamedQuery(ToDo.findAll, ToDo.class).getResultList();
    }

}
