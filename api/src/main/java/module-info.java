import org.jspecify.annotations.NullMarked;

@NullMarked
module net.thenextlvl.gopaintadvanced {
    exports net.thenextlvl.gopaint.api.brush.mask;
    exports net.thenextlvl.gopaint.api.brush.pattern;
    exports net.thenextlvl.gopaint.api.brush.setting;
    exports net.thenextlvl.gopaint.api.brush;
    exports net.thenextlvl.gopaint.api.math.curve;
    exports net.thenextlvl.gopaint.api.math;
    exports net.thenextlvl.gopaint.api.model;

    requires com.google.gson;
    requires core.paper;
    requires net.kyori.adventure.key;
    requires net.kyori.adventure;
    requires net.thenextlvl.i18n;
    requires org.bukkit;

    requires static org.jetbrains.annotations;
    requires static org.jspecify;
}