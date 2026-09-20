import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ICustRequestType } from 'app/entities/cust-request-type/cust-request-type.model';
import { CustRequestTypeService } from 'app/entities/cust-request-type/service/cust-request-type.service';
import { ICustRequestCategory } from '../cust-request-category.model';
import { CustRequestCategoryService } from '../service/cust-request-category.service';

import { CustRequestCategoryFormService } from './cust-request-category-form.service';
import { CustRequestCategoryUpdate } from './cust-request-category-update';

describe('CustRequestCategory Management Update Component', () => {
  let comp: CustRequestCategoryUpdate;
  let fixture: ComponentFixture<CustRequestCategoryUpdate>;
  let activatedRoute: ActivatedRoute;
  let custRequestCategoryFormService: CustRequestCategoryFormService;
  let custRequestCategoryService: CustRequestCategoryService;
  let custRequestTypeService: CustRequestTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(CustRequestCategoryUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    custRequestCategoryFormService = TestBed.inject(CustRequestCategoryFormService);
    custRequestCategoryService = TestBed.inject(CustRequestCategoryService);
    custRequestTypeService = TestBed.inject(CustRequestTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call CustRequestType query and add missing value', () => {
      const custRequestCategory: ICustRequestCategory = { id: 7246 };
      const custRequestType: ICustRequestType = { id: 4424 };
      custRequestCategory.custRequestType = custRequestType;

      const custRequestTypeCollection: ICustRequestType[] = [{ id: 4424 }];
      vi.spyOn(custRequestTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: custRequestTypeCollection })));
      const additionalCustRequestTypes = [custRequestType];
      const expectedCollection: ICustRequestType[] = [...additionalCustRequestTypes, ...custRequestTypeCollection];
      vi.spyOn(custRequestTypeService, 'addCustRequestTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ custRequestCategory });
      comp.ngOnInit();

      expect(custRequestTypeService.query).toHaveBeenCalled();
      expect(custRequestTypeService.addCustRequestTypeToCollectionIfMissing).toHaveBeenCalledWith(
        custRequestTypeCollection,
        ...additionalCustRequestTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.custRequestTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const custRequestCategory: ICustRequestCategory = { id: 7246 };
      const custRequestType: ICustRequestType = { id: 4424 };
      custRequestCategory.custRequestType = custRequestType;

      activatedRoute.data = of({ custRequestCategory });
      comp.ngOnInit();

      expect(comp.custRequestTypesSharedCollection()).toContainEqual(custRequestType);
      expect(comp.custRequestCategory).toEqual(custRequestCategory);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICustRequestCategory>();
      const custRequestCategory = { id: 30459 };
      vi.spyOn(custRequestCategoryFormService, 'getCustRequestCategory').mockReturnValue(custRequestCategory);
      vi.spyOn(custRequestCategoryService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ custRequestCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(custRequestCategory);
      saveSubject.complete();

      // THEN
      expect(custRequestCategoryFormService.getCustRequestCategory).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(custRequestCategoryService.update).toHaveBeenCalledWith(expect.objectContaining(custRequestCategory));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICustRequestCategory>();
      const custRequestCategory = { id: 30459 };
      vi.spyOn(custRequestCategoryFormService, 'getCustRequestCategory').mockReturnValue({ id: null });
      vi.spyOn(custRequestCategoryService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ custRequestCategory: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(custRequestCategory);
      saveSubject.complete();

      // THEN
      expect(custRequestCategoryFormService.getCustRequestCategory).toHaveBeenCalled();
      expect(custRequestCategoryService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ICustRequestCategory>();
      const custRequestCategory = { id: 30459 };
      vi.spyOn(custRequestCategoryService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ custRequestCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(custRequestCategoryService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCustRequestType', () => {
      it('should forward to custRequestTypeService', () => {
        const entity = { id: 4424 };
        const entity2 = { id: 951 };
        vi.spyOn(custRequestTypeService, 'compareCustRequestType');
        comp.compareCustRequestType(entity, entity2);
        expect(custRequestTypeService.compareCustRequestType).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
