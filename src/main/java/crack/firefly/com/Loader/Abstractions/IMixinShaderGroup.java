package crack.firefly.com.Loader.Abstractions;

import java.util.List;

import net.minecraft.client.shader.Shader;

public interface IMixinShaderGroup {
	List<Shader> getListShaders();
}
