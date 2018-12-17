package com.shulian.neo4j.controller;

import com.shulian.neo4j.domain.entity.Coder;
import com.shulian.neo4j.dto.Result;
import com.shulian.neo4j.service.ICoderService;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

/**
 * @author zhangjuwa
 * @description neo4j控制器
 * @date 2018/9/5
 * @since jdk1.8
 */
@RestController
public class CoderController {

    @Autowired
    private ICoderService coderService;

    @GetMapping("coders/{id}")
    public Result<Coder> get(@Valid @NotNull(message = "id不能为空") @PathVariable(name = "id") Long id) {
        return Result.<Coder>build().ok().withData(coderService.get(id));
    }

    @GetMapping("coders/{name}")
    public Result<List<Coder>> getList(@Valid @NotBlank(message = "名称不能为空") @PathVariable(name = "name") String name) {
        return Result.<List<Coder>>build().ok().withData(coderService.findByName(name));
    }

    @GetMapping("coders/ids")
    public Result<Set<Coder>> listByIds(@Valid @Size(min = 1, message = "id个数最小为1个") @RequestParam(required = false) List<Long> ids) {
        return Result.<Set<Coder>>build().ok().withData(coderService.listByIds(ids));
    }

    @GetMapping("coders")
    public Result<List<Coder>> list() {
        return Result.<List<Coder>>build().ok().withData(coderService.findAll());
    }

    @PostMapping("coders")
    public Result<Coder> add(@Valid @NotNull(message = "coder不能为空") @RequestBody Coder coder) {
        return Result.<Coder>build().ok().withData(coderService.add(coder));
    }

    @DeleteMapping("coders/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.<Boolean>build().ok().withData(coderService.delete(id));
    }

    @PutMapping("coders")
    public Result<Coder> update(@RequestBody Coder coder) {
        return Result.<Coder>build().ok().withData(coderService.update(coder));
    }
}
