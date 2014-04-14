package org.delafer.recoder.cli.compressor;

import org.delafer.recoder.helpers.utils.UtilsString;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

public class MetaDataExtractor {

	private Tag tag;

	public MetaDataExtractor(Tag tag) {
		this.tag = tag;
	}

	private String nvl(String v1, String v2) {
		return UtilsString.isEmpty(v1) ? v2 : v1;
	}

	private String getFirst(Object... key) {
		try {
			String ret = null;
			for (Object next : key) {
				if (next instanceof FieldKey) ret = tag.getFirst((FieldKey)next);
				else
				if (next instanceof String) ret = tag.getFirst((String)next);
				if (UtilsString.isNotEmpty(ret)) return ret;
			}
		} catch (Exception ignore) {}
		return null;
	}

	private String concat(String... arr) {
		if (null == arr || arr.length == 0) return "";

		StringBuilder sb = new StringBuilder();
		for (String next : arr) {
			if (UtilsString.isNotEmpty(next)) {
				if (sb.length()>0) sb.append(' ');
				sb.append(next);
			}
		}
		return sb.toString();
	}


	public final String get(final  FieldKey key) {
		final String ret = getInternal(key);
		if (null == ret || ret.isEmpty()) return ret;

		String rez = null;
		if (ret.indexOf('"')>=0)
			rez =  UtilsString.replace(ret, '"', "'");
		else
			rez = ret;

		return rez.trim();

	}

	public final  String getInternal(FieldKey key) {

		switch (key) {
		case COMMENT:
			return getFirst(FieldKey.COMMENT, "desc", FieldKey.CUSTOM1, FieldKey.COMPOSER, FieldKey.PRODUCER, FieldKey.ENCODER, FieldKey.ARRANGER);
		case YEAR:
			return nvl(getFirst(FieldKey.YEAR, "\u00a9day", FieldKey.ORIGINAL_YEAR), "2014");
		case TRACK:
			return concat(getFirst(FieldKey.TRACK, "trkn", FieldKey.MUSICBRAINZ_TRACK_ID), "/",getFirst(FieldKey.TRACK_TOTAL));
		case ARTIST:
			return concat(getFirst(FieldKey.ARTIST, "\u00a9ART", "aART", FieldKey.ALBUM_ARTIST, FieldKey.ORIGINAL_ARTIST, FieldKey.ARTISTS), getFirst(FieldKey.COMPOSER, "\u00a9wrt", FieldKey.PRODUCER, FieldKey.ARRANGER, FieldKey.ENGINEER));
		case TITLE:
			return concat(getFirst(FieldKey.TITLE, "\u00a9nam"), getFirst(FieldKey.SUBTITLE), getFirst(FieldKey.DISC_SUBTITLE), getFirst(FieldKey.RECORD_LABEL));
		case ALBUM:
			return getFirst(FieldKey.ALBUM, "\u00a9alb", FieldKey.ORIGINAL_ALBUM, "\u00a9art");
		case GENRE:
			return getFirst(FieldKey.GENRE, "\u00a9gen", FieldKey.MUSICBRAINZ_RELEASE_TYPE );
		default:
			break;
		}
		return "";
	}

}
