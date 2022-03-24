package net.kunmc.lab.configlib.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraft.util.math.BlockPos;

import java.io.IOException;

public class BlockPosTypeAdapter extends TypeAdapter<BlockPos> {
    @Override
    public void write(JsonWriter out, BlockPos value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        out.beginObject();

        out.name("x").value(value.getX());
        out.name("y").value(value.getY());
        out.name("z").value(value.getZ());

        out.endObject();
    }

    @Override
    public BlockPos read(JsonReader in) throws IOException {
        in.beginObject();

        in.nextName();
        int x = in.nextInt();

        in.nextName();
        int y = in.nextInt();

        in.nextName();
        int z = in.nextInt();

        in.endObject();
        return new BlockPos(x, y, z);
    }
}
