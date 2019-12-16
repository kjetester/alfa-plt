package ru.alfabank.platform.businessobjects.draft.widget;

import com.fasterxml.jackson.annotation.*;
import ru.alfabank.platform.businessobjects.*;

import java.util.*;

@JsonAutoDetect (fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "version", "operations"})
public class WidgetDraft {

	private List<Object> operations;
	private String version;

	public WidgetDraft(String version, List<Object> operations) {
		this.version = version;
		this.operations = operations;
	}

	@JsonAutoDetect (fieldVisibility = JsonAutoDetect.Visibility.ANY)
	@JsonInclude (JsonInclude.Include.NON_NULL)
	@JsonPropertyOrder({ "uid", "entity", "method", "data"})
	public static class Operations {

		private Data data;
		private Entity entity;
		private Method method;
		private String uid;

		public Operations(String widgetUid, Entity entity, Method method, Data data) {
			this.uid = widgetUid;
			this.entity = entity;
			this.method = method;
			this.data = data;
		}

		@JsonAutoDetect (fieldVisibility = JsonAutoDetect.Visibility.ANY)
		@JsonInclude (JsonInclude.Include.NON_NULL)
		@JsonPropertyOrder({ "dateFrom", "dateTo", "device", "enable", "localization", "state", "name", "cityGroups"})
		public static class Data {

			private String dateFrom;
			private String dateTo;
			private Device device;
			private boolean enable;
			private String localization;
			private String state;
			private String name;
			private String[] cityGroups;

			public Data(String dateFrom, String dateTo, Device device,
			            boolean enable, String localization, String state, String name, String[] cityGroups) {
				this.dateFrom = dateFrom;
				this.dateTo = dateTo;
				this.device = device;
				this.enable = enable;
				this.localization = localization;
				this.state = state;
				this.name = name;
				this.cityGroups = cityGroups;
			}
		}
	}
}
