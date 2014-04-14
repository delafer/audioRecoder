package org.delafer.recoder.model.enums;

import org.delafer.recoder.model.enums.factory.Register;

public enum AudioType {

	mp3,
	wav,
	pcm,
	raw,
	ape, //Monkey's Audio
	flac,
	fla,
	wv, //WavPack (.wv .wvc)
	aud,
	aif,
	ra,
	rm,
	wma,
	mpc, //Musepack
	mp("mp+"),//Musepack
	mpp,//Musepack
	opus,
	m4a,
	m4b,
	aac,
	ofs,
	ofr, //OptimFROG
	spx,
	mac,
	ogg,
	shn, //Shorten (.shn)
	tta, //True Audio
	unknown
	;

	private String extension;

	AudioType() {
		this(null);
	}

	AudioType(String extension) {
		this.extension = (extension == null ? this.name() : extension);
		Register.register(this, this.extension.toLowerCase());
	}

	public String getExtension() {
		return this.extension;
	}

	public static AudioType getByExt(String extension) {
		if (null == extension) return AudioType.unknown;
		AudioType ret = (AudioType)Register.getByKey(unknown, extension.toLowerCase());
		return (ret != null) ? ret : unknown;
	}


}
