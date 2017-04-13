package de.dis2011.dao;

import de.dis2011.data.EstateAgent;

public interface EstateAgentDAO extends EntryDAO<EstateAgent> {
	boolean loadByLogin (EstateAgent entity);
}
