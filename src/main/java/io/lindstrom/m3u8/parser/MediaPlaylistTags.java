package io.lindstrom.m3u8.parser;

import io.lindstrom.m3u8.model.MediaPlaylist;
import io.lindstrom.m3u8.model.PlaylistType;
import io.lindstrom.m3u8.model.StartTimeOffset;

import java.util.Iterator;

import static io.lindstrom.m3u8.parser.AbstractPlaylistParser.readAttributes;
import static io.lindstrom.m3u8.parser.Tags.*;

enum MediaPlaylistTags implements Tag<MediaPlaylist, MediaPlaylist.Builder> {
    EXT_X_VERSION {
        @Override
        public void read(MediaPlaylist.Builder builder, String attributes, Iterator<String> lineIterator) {
            builder.version(Integer.parseInt(attributes));
        }

        @Override
        public void write(MediaPlaylist playlist, TextBuilder textBuilder) {
            playlist.version().ifPresent(version -> textBuilder.add(tag())
                    .add(":").add(version).add('\n'));
        }
    },

    EXT_X_INDEPENDENT_SEGMENTS {
        @Override
        public void read(MediaPlaylist.Builder builder, String attributes, Iterator<String> lineIterator) {
            builder.independentSegments(true);
        }

        @Override
        public void write(MediaPlaylist playlist, TextBuilder textBuilder) {
            if (playlist.independentSegments()) {
                textBuilder.add(tag()).add('\n');
            }
        }
    },

    EXT_X_PLAYLIST_TYPE {
        @Override
        public void read(MediaPlaylist.Builder builder, String attributes, Iterator<String> lineIterator) {
            builder.playlistType(PlaylistType.valueOf(attributes));
        }

        @Override
        public void write(MediaPlaylist playlist, TextBuilder textBuilder) {
            playlist.playlistType().ifPresent(value ->
                    textBuilder.add(tag()).add(":").add(value.toString()).add('\n'));
        }
    },

    EXT_X_I_FRAMES_ONLY {
        @Override
        public void read(MediaPlaylist.Builder builder, String attributes, Iterator<String> lineIterator) {
            builder.iFramesOnly(true);
        }

        @Override
        public void write(MediaPlaylist playlist, TextBuilder textBuilder) {
            if (playlist.iFramesOnly()) {
                textBuilder.add(tag()).add("\n");
            }
        }
    },

    EXT_X_TARGETDURATION {
        @Override
        public void read(MediaPlaylist.Builder builder, String attributes, Iterator<String> lineIterator) {
            builder.targetDuration(Integer.parseInt(attributes));
        }

        @Override
        public void write(MediaPlaylist playlist, TextBuilder textBuilder) {
            textBuilder
                    .add(tag()).add(":")
                    .add(playlist.targetDuration()).add("\n");
        }
    },

    EXT_X_ENDLIST {
        @Override
        public void read(MediaPlaylist.Builder builder, String attributes, Iterator<String> lineIterator) {
            builder.ongoing(false);
        }

        @Override
        public void write(MediaPlaylist playlist, TextBuilder textBuilder) {
            // written elsewhere
        }
    },

    EXT_X_START {
        @Override
        public void read(MediaPlaylist.Builder builder, String attributes, Iterator<String> lineIterator) throws PlaylistParserException {
            builder.startTimeOffset(readAttributes(StartTimeOffsetParser.class, attributes, StartTimeOffset.builder()).build());
        }

        @Override
        public void write(MediaPlaylist playlist, TextBuilder textBuilder) {
            textBuilder.add(tag(), playlist.startTimeOffset(), StartTimeOffsetParser.class);
        }
    },

    EXT_X_DISCONTINUITY_SEQUENCE {
        @Override
        public void read(MediaPlaylist.Builder builder, String attributes, Iterator<String> lineIterator) {
            builder.discontinuitySequence(Long.parseLong(attributes));
        }

        @Override
        public void write(MediaPlaylist playlist, TextBuilder textBuilder) {
            if (playlist.discontinuitySequence() != 0) {
                textBuilder.add(tag()).add(":").add(playlist.discontinuitySequence()).add("\n");
            }
        }
    },

    EXT_X_MEDIA_SEQUENCE {
        @Override
        public void read(MediaPlaylist.Builder builder, String attributes, Iterator<String> lineIterator) {
            builder.mediaSequence(Long.parseLong(attributes));

        }

        @Override
        public void write(MediaPlaylist playlist, TextBuilder textBuilder) {
            textBuilder.add(tag()).add(":").add(playlist.mediaSequence()).add("\n");
        }
    },

    EXT_X_ALLOW_CACHE {
        @Override
        public void read(MediaPlaylist.Builder builder, String attributes, Iterator<String> lineIterator) throws PlaylistParserException {
            builder.allowCache(ParserUtils.yesOrNo(attributes));
        }

        @Override
        public void write(MediaPlaylist playlist, TextBuilder textBuilder) {
            playlist.allowCache().ifPresent(value ->
                    textBuilder.add(tag()).add(":").add(value ? YES : NO).add("\n")
            );
        }
    }
}
