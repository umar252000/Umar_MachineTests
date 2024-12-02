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
import com.umar.machinetests.dto.ProductDto;
import com.umar.machinetests.payload.AppConstant;
import com.umar.machinetests.service.ProductMethod;
import com.umar.machinetests.service.impl.CategoryService;
import com.umar.machinetests.service.impl.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	@Autowired
	ProductMethod productService;
	
	@PostMapping
	public ResponseEntity<ProductDto> insert( @RequestBody ProductDto productDto)
	{
		ProductDto insert = this.productService.insert(productDto);
		return new ResponseEntity(insert,HttpStatus.CREATED);
		
	}
	

	@PutMapping("{id}")
	public ResponseEntity<ProductDto> update(@RequestBody ProductDto productDto,@PathVariable("id")long id)
	{
		 ProductDto update = this.productService.update(productDto, id);
		return new ResponseEntity(update,HttpStatus.OK);
	}
	

	@DeleteMapping("{id}")
	public ResponseEntity<String> delete(@PathVariable("id")Long id)
	{
		 this.productService.delete(id);
		 return new ResponseEntity("id="+id+" deleted successfull",HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ProductDto> get(@PathVariable("id") Long id)
	{
		ProductDto insert = this.productService.get(id);
		return new ResponseEntity(insert,HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<List<ProductDto>> insert(
			@RequestParam(value = "page",defaultValue = AppConstant.PAGE,required = false)Integer page,
			@RequestParam(value = "pagesize",defaultValue = AppConstant.PAGE_SIZE,required = false)Integer pagesize)
	{
		List<ProductDto> list = this.productService.get(page,pagesize);
		return new ResponseEntity(list,HttpStatus.CREATED);
	}
	
	
	
}
