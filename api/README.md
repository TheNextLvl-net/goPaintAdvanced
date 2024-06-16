## Repository

To seamlessly integrate our API into your project, please visit
our [repository](https://repo.thenextlvl.net/#/releases/net/thenextlvl/gopaint/api).
Select your preferred build tool and incorporate the version you intend to use.

### Gradle Example

For Gradle users, add the repository and dependency to your `build.gradle.kts`:

```kt
repositories {
    maven("https://repo.thenextlvl.net/releases")
}

dependencies {
    compileOnly("net.thenextlvl.gopaint:api:version")
}
```

> [!NOTE]
> Be sure to replace `version` with the actual version number.<br>
> The correct version of FastAsyncWorldEdit will be provided automatically<br>
> Also you shouldn't shade the API into your Jar as it is already provided by the plugin.

## API Documentation

### The BrushController

With the brush controller you can manage and retrieve brush settings from players and items

```java
BrushController controller = Bukkit.getServicesManager().load(BrushController.class);
```

### The BrushRegistry

The brush registry allows you to register, unregister and retrieve brushes

```java
BrushRegistry registry = Bukkit.getServicesManager().load(BrushRegistry.class);
```

To register a new brush you have to invoke `BrushRegistry#register`

### Creating new brushes

In order to create a new brush you have to extend `Brush`,
you need to provide a name, description and player head value.<br>
In addition to that you have to overwrite the `Brush#paint` method.
In there you perform all of your calculations and edits.<br>
We recommend using `Brush#performEdit` to retrieve an edit session and `Brush#setBlock` to manipulate blocks,
as it immediately frees resources after execution.

For ease of use we provide some math classes for you:

- Curve
    - BezierSpline
    - BezierSplineSegment
- ConnectedBlocks
- Height
- Sphere
- Surface

> [!TIP]
> You can always take a look at our default implementations of brushes if you need help

