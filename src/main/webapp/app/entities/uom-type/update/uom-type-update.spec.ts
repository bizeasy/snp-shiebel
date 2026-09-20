import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { UomTypeService } from '../service/uom-type.service';
import { IUomType } from '../uom-type.model';

import { UomTypeFormService } from './uom-type-form.service';
import { UomTypeUpdate } from './uom-type-update';

describe('UomType Management Update Component', () => {
  let comp: UomTypeUpdate;
  let fixture: ComponentFixture<UomTypeUpdate>;
  let activatedRoute: ActivatedRoute;
  let uomTypeFormService: UomTypeFormService;
  let uomTypeService: UomTypeService;

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

    fixture = TestBed.createComponent(UomTypeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    uomTypeFormService = TestBed.inject(UomTypeFormService);
    uomTypeService = TestBed.inject(UomTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const uomType: IUomType = { id: 18459 };

      activatedRoute.data = of({ uomType });
      comp.ngOnInit();

      expect(comp.uomType).toEqual(uomType);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IUomType>();
      const uomType = { id: 28679 };
      vi.spyOn(uomTypeFormService, 'getUomType').mockReturnValue(uomType);
      vi.spyOn(uomTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ uomType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(uomType);
      saveSubject.complete();

      // THEN
      expect(uomTypeFormService.getUomType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(uomTypeService.update).toHaveBeenCalledWith(expect.objectContaining(uomType));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IUomType>();
      const uomType = { id: 28679 };
      vi.spyOn(uomTypeFormService, 'getUomType').mockReturnValue({ id: null });
      vi.spyOn(uomTypeService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ uomType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(uomType);
      saveSubject.complete();

      // THEN
      expect(uomTypeFormService.getUomType).toHaveBeenCalled();
      expect(uomTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IUomType>();
      const uomType = { id: 28679 };
      vi.spyOn(uomTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ uomType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(uomTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
