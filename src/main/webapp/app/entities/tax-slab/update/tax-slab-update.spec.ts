import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { TaxSlabService } from '../service/tax-slab.service';
import { ITaxSlab } from '../tax-slab.model';

import { TaxSlabFormService } from './tax-slab-form.service';
import { TaxSlabUpdate } from './tax-slab-update';

describe('TaxSlab Management Update Component', () => {
  let comp: TaxSlabUpdate;
  let fixture: ComponentFixture<TaxSlabUpdate>;
  let activatedRoute: ActivatedRoute;
  let taxSlabFormService: TaxSlabFormService;
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

    fixture = TestBed.createComponent(TaxSlabUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    taxSlabFormService = TestBed.inject(TaxSlabFormService);
    taxSlabService = TestBed.inject(TaxSlabService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const taxSlab: ITaxSlab = { id: 24819 };

      activatedRoute.data = of({ taxSlab });
      comp.ngOnInit();

      expect(comp.taxSlab).toEqual(taxSlab);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ITaxSlab>();
      const taxSlab = { id: 22491 };
      vi.spyOn(taxSlabFormService, 'getTaxSlab').mockReturnValue(taxSlab);
      vi.spyOn(taxSlabService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ taxSlab });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(taxSlab);
      saveSubject.complete();

      // THEN
      expect(taxSlabFormService.getTaxSlab).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(taxSlabService.update).toHaveBeenCalledWith(expect.objectContaining(taxSlab));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ITaxSlab>();
      const taxSlab = { id: 22491 };
      vi.spyOn(taxSlabFormService, 'getTaxSlab').mockReturnValue({ id: null });
      vi.spyOn(taxSlabService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ taxSlab: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(taxSlab);
      saveSubject.complete();

      // THEN
      expect(taxSlabFormService.getTaxSlab).toHaveBeenCalled();
      expect(taxSlabService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ITaxSlab>();
      const taxSlab = { id: 22491 };
      vi.spyOn(taxSlabService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ taxSlab });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(taxSlabService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
