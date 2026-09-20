import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { UomTypeService } from 'app/entities/uom-type/service/uom-type.service';
import { IUomType } from 'app/entities/uom-type/uom-type.model';
import { UomService } from '../service/uom.service';
import { IUom } from '../uom.model';

import { UomFormService } from './uom-form.service';
import { UomUpdate } from './uom-update';

describe('Uom Management Update Component', () => {
  let comp: UomUpdate;
  let fixture: ComponentFixture<UomUpdate>;
  let activatedRoute: ActivatedRoute;
  let uomFormService: UomFormService;
  let uomService: UomService;
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

    fixture = TestBed.createComponent(UomUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    uomFormService = TestBed.inject(UomFormService);
    uomService = TestBed.inject(UomService);
    uomTypeService = TestBed.inject(UomTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call UomType query and add missing value', () => {
      const uom: IUom = { id: 20168 };
      const uomType: IUomType = { id: 28679 };
      uom.uomType = uomType;

      const uomTypeCollection: IUomType[] = [{ id: 28679 }];
      vi.spyOn(uomTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: uomTypeCollection })));
      const additionalUomTypes = [uomType];
      const expectedCollection: IUomType[] = [...additionalUomTypes, ...uomTypeCollection];
      vi.spyOn(uomTypeService, 'addUomTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ uom });
      comp.ngOnInit();

      expect(uomTypeService.query).toHaveBeenCalled();
      expect(uomTypeService.addUomTypeToCollectionIfMissing).toHaveBeenCalledWith(
        uomTypeCollection,
        ...additionalUomTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.uomTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const uom: IUom = { id: 20168 };
      const uomType: IUomType = { id: 28679 };
      uom.uomType = uomType;

      activatedRoute.data = of({ uom });
      comp.ngOnInit();

      expect(comp.uomTypesSharedCollection()).toContainEqual(uomType);
      expect(comp.uom).toEqual(uom);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IUom>();
      const uom = { id: 6576 };
      vi.spyOn(uomFormService, 'getUom').mockReturnValue(uom);
      vi.spyOn(uomService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ uom });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(uom);
      saveSubject.complete();

      // THEN
      expect(uomFormService.getUom).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(uomService.update).toHaveBeenCalledWith(expect.objectContaining(uom));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IUom>();
      const uom = { id: 6576 };
      vi.spyOn(uomFormService, 'getUom').mockReturnValue({ id: null });
      vi.spyOn(uomService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ uom: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(uom);
      saveSubject.complete();

      // THEN
      expect(uomFormService.getUom).toHaveBeenCalled();
      expect(uomService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IUom>();
      const uom = { id: 6576 };
      vi.spyOn(uomService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ uom });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(uomService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUomType', () => {
      it('should forward to uomTypeService', () => {
        const entity = { id: 28679 };
        const entity2 = { id: 18459 };
        vi.spyOn(uomTypeService, 'compareUomType');
        comp.compareUomType(entity, entity2);
        expect(uomTypeService.compareUomType).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
