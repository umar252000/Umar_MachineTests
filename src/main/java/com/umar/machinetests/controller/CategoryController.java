package com.umar.machinetests.controller;

import java.util.List;

import org.modelmapper.internal.bytebuddy.implementation.bytecode.constant.DefaultValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.JsonPath;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.umar.machinetests.dto.CategoryDto;
import com.umar.machinetests.payload.AppConstant;
import com.umar.machinetests.service.CategoryMethod;
import com.umar.machinetests.service.impl.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

	@Autowired
	CategoryMethod crudService;
	
	@PostMapping
	public ResponseEntity<CategoryDto> insert( @RequestBody CategoryDto crudDto) throws Exception
	{
		CategoryDto insert = this.crudService.insert(crudDto);
		return new ResponseEntity(insert,HttpStatus.CREATED);
		
	}
	

	@PutMapping("{id}")
	public ResponseEntity<CategoryDto> update(@RequestBody CategoryDto crudDto,@PathVariable("id")long id)
	{
		 CategoryDto update = this.crudService.update(crudDto, id);
		return new ResponseEntity(update,HttpStatus.OK);
	}
	

	@DeleteMapping("{id}")
	public ResponseEntity<String> delete(@PathVariable("id")Long id)
	{
		 this.crudService.delete(id);
		 return new ResponseEntity("id="+id+" deleted successfull",HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<CategoryDto> get(@PathVariable("id") Long id)
	{
		CategoryDto insert = this.crudService.get(id);
		return new ResponseEntity(insert,HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<List<CategoryDto>> insert(
			@RequestParam(value = "page",defaultValue = AppConstant.PAGE,required = false)Integer page,
			@RequestParam(value = "pagesize",defaultValue = AppConstant.PAGE_SIZE,required = false)Integer pagesize)
	{
		List<CategoryDto> list = this.crudService.get(page,pagesize);
		return new ResponseEntity(list,HttpStatus.CREATED);
	}
	
	
	
}
