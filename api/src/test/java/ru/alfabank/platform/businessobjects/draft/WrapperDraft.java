package ru.alfabank.platform.businessobjects.draft;

import com.fasterxml.jackson.annotation.*;
import ru.alfabank.platform.businessobjects.*;

import java.util.*;

@JsonAutoDetect (fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WrapperDraft {
	private final List<Object> operations;
	private final String version;
	private final Device device;

	@JsonCreator
	public WrapperDraft(List<Object> operations) {
		this.operations = operations;
		this.version = "01";
		this.device = Device.desktop;
	}

	@JsonAutoDetect (fieldVisibility = JsonAutoDetect.Visibility.ANY)
	@JsonInclude (JsonInclude.Include.NON_NULL)
	public static class OperationDraft {

		private final Object data;
		private final Entity entity;
		private final Method method;
		private final String uid;

		@JsonCreator
		public OperationDraft(Object data, Entity entity, Method method, String uid) {
			this.data = data;
			this.entity = entity;
			this.method = method;
			this.uid = uid;
		}
	}
}

