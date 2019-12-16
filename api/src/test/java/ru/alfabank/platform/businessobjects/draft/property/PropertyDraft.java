package ru.alfabank.platform.businessobjects.draft.property;

import com.fasterxml.jackson.annotation.*;
import ru.alfabank.platform.businessobjects.*;

import java.util.*;

@JsonAutoDetect (fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PropertyDraft {

	private List<Object> operations;
	private String version;

	public PropertyDraft(List<Object> operations, String version) {
		this.operations = operations;
		this.version = version;
	}

	@JsonAutoDetect (fieldVisibility = JsonAutoDetect.Visibility.ANY)
	@JsonInclude (JsonInclude.Include.NON_NULL)
	public static class Operations {

		private Data data;
		private Entity entity;
		private Method method;
		private String uid;

		public Operations(Data data, Entity entity, Method method, String propertyUid) {
			this.data = data;
			this.entity = entity;
			this.method = method;
			this.uid = propertyUid;
		}

		@JsonAutoDetect (fieldVisibility = JsonAutoDetect.Visibility.ANY)
		@JsonInclude (JsonInclude.Include.NON_NULL)
		public static class Data {

			private String widgetUid;
			private String name;
			private Device device;

			public Data(String widgetUid, String name, Device device) {
				this.widgetUid = widgetUid;
				this.name = name;
				this.device = device;
			}
		}
	}
}
