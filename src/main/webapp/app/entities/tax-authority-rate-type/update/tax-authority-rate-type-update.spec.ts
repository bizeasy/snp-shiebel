import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { TaxAuthorityService } from 'app/entities/tax-authority/service/tax-authority.service';
import { ITaxAuthority } from 'app/entities/tax-authority/tax-authority.model';
import { TaxSlabService } from 'app/entities/tax-slab/service/tax-slab.service';
import { ITaxSlab } from 'app/entities/tax-slab/tax-slab.model';
import { TaxAuthorityRateTypeService } from '../service/tax-authority-rate-type.service';
import { ITaxAuthorityRateType } from '../tax-authority-rate-type.model';

import { TaxAuthorityRateTypeFormService } from './tax-authority-rate-type-form.service';
import { TaxAuthorityRateTypeUpdate } from './tax-authority-rate-type-update';

describe('TaxAuthorityRateType Management Update Component', () => {
  let comp: TaxAuthorityRateTypeUpdate;
  let fixture: ComponentFixture<TaxAuthorityRateTypeUpdate>;
  let activatedRoute: ActivatedRoute;
  let taxAuthorityRateTypeFormService: TaxAuthorityRateTypeFormService;
  let taxAuthorityRateTypeService: TaxAuthorityRateTypeService;
  let taxAuthorityService: TaxAuthorityService;
  let taxSlabService: TaxSlabService;

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

    fixture = TestBed.createComponent(TaxAuthorityRateTypeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    taxAuthorityRateTypeFormService = TestBed.inject(TaxAuthorityRateTypeFormService);
    taxAuthorityRateTypeService = TestBed.inject(TaxAuthorityRateTypeService);
    taxAuthorityService = TestBed.inject(TaxAuthorityService);
    taxSlabService = TestBed.inject(TaxSlabService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call TaxAuthority query and add missing value', () => {
      const taxAuthorityRateType: ITaxAuthorityRateType = { id: 3035 };
      const taxAuthority: ITaxAuthority = { id: 14408 };
      taxAuthorityRateType.taxAuthority = taxAuthority;

      const taxAuthorityCollection: ITaxAuthority[] = [{ id: 14408 }];
      vi.spyOn(taxAuthorityService, 'query').mockReturnValue(of(new HttpResponse({ body: taxAuthorityCollection })));
      const additionalTaxAuthorities = [taxAuthority];
      const expectedCollection: ITaxAuthority[] = [...additionalTaxAuthorities, ...taxAuthorityCollection];
      vi.spyOn(taxAuthorityService, 'addTaxAuthorityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ taxAuthorityRateType });
      comp.ngOnInit();

      expect(taxAuthorityService.query).toHaveBeenCalled();
      expect(taxAuthorityService.addTaxAuthorityToCollectionIfMissing).toHaveBeenCalledWith(
        taxAuthorityCollection,
        ...additionalTaxAuthorities.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.taxAuthoritiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call TaxSlab query and add missing value', () => {
      const taxAuthorityRateType: ITaxAuthorityRateType = { id: 3035 };
      const taxSlab: ITaxSlab = { id: 22491 };
      taxAuthorityRateType.taxSlab = taxSlab;

      const taxSlabCollection: ITaxSlab[] = [{ id: 22491 }];
      vi.spyOn(taxSlabService, 'query').mockReturnValue(of(new HttpResponse({ body: taxSlabCollection })));
      const additionalTaxSlabs = [taxSlab];
      const expectedCollection: ITaxSlab[] = [...additionalTaxSlabs, ...taxSlabCollection];
      vi.spyOn(taxSlabService, 'addTaxSlabToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ taxAuthorityRateType });
      comp.ngOnInit();

      expect(taxSlabService.query).toHaveBeenCalled();
      expect(taxSlabService.addTaxSlabToCollectionIfMissing).toHaveBeenCalledWith(
        taxSlabCollection,
        ...additionalTaxSlabs.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.taxSlabsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const taxAuthorityRateType: ITaxAuthorityRateType = { id: 3035 };
      const taxAuthority: ITaxAuthority = { id: 14408 };
      taxAuthorityRateType.taxAuthority = taxAuthority;
      const taxSlab: ITaxSlab = { id: 22491 };
      taxAuthorityRateType.taxSlab = taxSlab;

      activatedRoute.data = of({ taxAuthorityRateType });
      comp.ngOnInit();

      expect(comp.taxAuthoritiesSharedCollection()).toContainEqual(taxAuthority);
      expect(comp.taxSlabsSharedCollection()).toContainEqual(taxSlab);
      expect(comp.taxAuthorityRateType).toEqual(taxAuthorityRateType);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ITaxAuthorityRateType>();
      const taxAuthorityRateType = { id: 11026 };
      vi.spyOn(taxAuthorityRateTypeFormService, 'getTaxAuthorityRateType').mockReturnValue(taxAuthorityRateType);
      vi.spyOn(taxAuthorityRateTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ taxAuthorityRateType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(taxAuthorityRateType);
      saveSubject.complete();

      // THEN
      expect(taxAuthorityRateTypeFormService.getTaxAuthorityRateType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(taxAuthorityRateTypeService.update).toHaveBeenCalledWith(expect.objectContaining(taxAuthorityRateType));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ITaxAuthorityRateType>();
      const taxAuthorityRateType = { id: 11026 };
      vi.spyOn(taxAuthorityRateTypeFormService, 'getTaxAuthorityRateType').mockReturnValue({ id: null });
      vi.spyOn(taxAuthorityRateTypeService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ taxAuthorityRateType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(taxAuthorityRateType);
      saveSubject.complete();

      // THEN
      expect(taxAuthorityRateTypeFormService.getTaxAuthorityRateType).toHaveBeenCalled();
      expect(taxAuthorityRateTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ITaxAuthorityRateType>();
      const taxAuthorityRateType = { id: 11026 };
      vi.spyOn(taxAuthorityRateTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ taxAuthorityRateType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(taxAuthorityRateTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTaxAuthority', () => {
      it('should forward to taxAuthorityService', () => {
        const entity = { id: 14408 };
        const entity2 = { id: 27648 };
        vi.spyOn(taxAuthorityService, 'compareTaxAuthority');
        comp.compareTaxAuthority(entity, entity2);
        expect(taxAuthorityService.compareTaxAuthority).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareTaxSlab', () => {
      it('should forward to taxSlabService', () => {
        const entity = { id: 22491 };
        const entity2 = { id: 24819 };
        vi.spyOn(taxSlabService, 'compareTaxSlab');
        comp.compareTaxSlab(entity, entity2);
        expect(taxSlabService.compareTaxSlab).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
