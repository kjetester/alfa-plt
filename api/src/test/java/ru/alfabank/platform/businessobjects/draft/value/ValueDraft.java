package ru.alfabank.platform.businessobjects.draft.value;

import com.fasterxml.jackson.annotation.*;
import ru.alfabank.platform.businessobjects.*;

import java.util.*;

@JsonAutoDetect (fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValueDraft {

	private List<Object> operations;
	private String version;

	public ValueDraft(List<Object> operations, String version) {
		this.operations = operations;
		this.version = version;
	}

	@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class Operations {

		private Object data;
		private Entity entity;
		private Method method;
		private String uid;

		public Operations(Object data, Entity entity, Method method, String propertyValueUid) {
			this.data = data;
			this.entity = entity;
			this.method = method;
			this.uid = propertyValueUid;
		}

		@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
		@JsonInclude(JsonInclude.Include.NON_NULL)
		public static class Data {

			private String[] cityGroups;
			private String propertyUid;
			private Object value;

			public Data(String[] cityGroups, String propertyUid, Object value) {
				this.cityGroups = cityGroups;
				this.propertyUid = propertyUid;
				this.value = value;
			}
		}
	}
}
