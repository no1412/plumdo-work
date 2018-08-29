package com.plumdo.form.resource.table;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.plumdo.common.jpa.Criteria;
import com.plumdo.common.jpa.Restrictions;
import com.plumdo.common.resource.BaseResource;
import com.plumdo.common.resource.PageResponse;
import com.plumdo.form.constant.ErrorConstant;
import com.plumdo.form.domain.FormLayout;
import com.plumdo.form.repository.FormLayoutRepository;

/**
 * 数据表资源类
 *
 * @author wengwh
 * @date 2018年8月29日
 */
@RestController
public class FormLayoutResource extends BaseResource {
	@Autowired
	private FormLayoutRepository formLayoutRepository;
	
	private FormLayout getFormLayoutFromRequest(Integer id) {
		FormLayout formLayout = formLayoutRepository.findOne(id);
		if (formLayout == null) {
			exceptionFactory.throwObjectNotFound(ErrorConstant.FORM_TABLE_NOT_FOUND);
		}
		return formLayout;
	}

	@GetMapping(value = "/form-layouts")
	@ResponseStatus(value = HttpStatus.OK)
	public PageResponse getFormLayouts(@RequestParam Map<String, String> requestParams) {
		Criteria<FormLayout> criteria = new Criteria<FormLayout>();
		criteria.add(Restrictions.eq("id", requestParams.get("id")));
		criteria.add(Restrictions.eq("tableId", requestParams.get("tableId")));
		criteria.add(Restrictions.like("name", requestParams.get("name")));
		criteria.add(Restrictions.like("remark", requestParams.get("remark")));
		criteria.add(Restrictions.like("tenantId", requestParams.get("tenantId")));
		return createPageResponse(formLayoutRepository.findAll(criteria, getPageable(requestParams)));
	}

	@GetMapping(value = "/form-layouts/{id}")
	@ResponseStatus(value = HttpStatus.OK)
	public FormLayout getFormLayout(@PathVariable Integer id) {
		return getFormLayoutFromRequest(id);
	}

	@PostMapping("/form-layouts")
	@ResponseStatus(HttpStatus.CREATED)
	public FormLayout createFormLayout(@RequestBody FormLayout formLayoutRequest) {
		return formLayoutRepository.save(formLayoutRequest);
	}

	@PutMapping(value = "/form-layouts/{id}")
	@ResponseStatus(value = HttpStatus.OK)
	public FormLayout updateFormLayout(@PathVariable Integer id, @RequestBody FormLayout formLayoutRequest) {
		FormLayout formLayout = getFormLayoutFromRequest(id);
		formLayout.setName(formLayoutRequest.getName());
		formLayout.setTenantId(formLayoutRequest.getTenantId());
		return formLayoutRepository.save(formLayout);
	}

	@DeleteMapping(value = "/form-layouts/{id}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteFormLayout(@PathVariable Integer id) {
		FormLayout formLayout = getFormLayoutFromRequest(id);
		formLayoutRepository.delete(formLayout);
	}
}
