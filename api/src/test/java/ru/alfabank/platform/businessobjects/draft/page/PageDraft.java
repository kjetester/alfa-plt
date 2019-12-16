package ru.alfabank.platform.businessobjects.draft.page;

import com.fasterxml.jackson.annotation.*;
import ru.alfabank.platform.businessobjects.*;

import java.util.*;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageDraft {

	private List<Object> operations;
	private String version;

	public PageDraft(
			List<Object> operations,
			String version) {
		this.operations = operations;
		this.version = version;
	}

	@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class Operations {

		private Data data;
		private Entity entity;
		private Method method;
		private String uid;

		public Operations(
				Data data,
				Entity entity,
				Method method,
				String uid) {
			this.data = data;
			this.entity = entity;
			this.method = method;
			this.uid = uid;
		}
			@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
			@JsonInclude(JsonInclude.Include.NON_NULL)
			public static class Data {

			private Object[] childUids;

				public Data(Object[] childUids) {
					this.childUids = childUids;
				}
			}
	}
}
