package org.delafer.recoder.model.enums;

import org.delafer.recoder.model.enums.factory.Register;


public enum SampleRate {

	SR_6000(6000, 1),
	SR_8000(8000, 2),
	SR_11025(11025, 3),
	SR_12000(12000, 4),
	SR_16000(16000, 5),
	SR_22050(22050, 6),
	SR_24000(24000, 7),
	SR_32000(32000, 8),
	SR_44100(44100, 9),
	SR_48000(48000, 10),
	SR_64000(64000, 11),
	SR_88200(88200, 12),
	SR_96000(96000, 13),
	SR_192000(192000, 14),
	SR_OTHER(-1, -1);

	int sampleRate;
	int id;


	SampleRate(int sampleRate, int id) {
		this.sampleRate = sampleRate;
		this.id = id;
		Register.register(this, sampleRate);
	}

	public int getSampleRate() {
		return sampleRate;
	}

	public int getId() {
		return id;
	}

	public static SampleRate getByRate(int sampleRate, SampleRate defValue) {
		SampleRate ret = (SampleRate)Register.getByKey(SR_OTHER, sampleRate);
		return (ret != null) ? ret : defValue;
	}


}
