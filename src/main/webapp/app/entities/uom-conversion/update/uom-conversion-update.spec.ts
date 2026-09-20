import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { UomService } from 'app/entities/uom/service/uom.service';
import { IUom } from 'app/entities/uom/uom.model';
import { UomConversionService } from '../service/uom-conversion.service';
import { IUomConversion } from '../uom-conversion.model';

import { UomConversionFormService } from './uom-conversion-form.service';
import { UomConversionUpdate } from './uom-conversion-update';

describe('UomConversion Management Update Component', () => {
  let comp: UomConversionUpdate;
  let fixture: ComponentFixture<UomConversionUpdate>;
  let activatedRoute: ActivatedRoute;
  let uomConversionFormService: UomConversionFormService;
  let uomConversionService: UomConversionService;
  let uomService: UomService;

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

    fixture = TestBed.createComponent(UomConversionUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    uomConversionFormService = TestBed.inject(UomConversionFormService);
    uomConversionService = TestBed.inject(UomConversionService);
    uomService = TestBed.inject(UomService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Uom query and add missing value', () => {
      const uomConversion: IUomConversion = { id: 18422 };
      const fromUom: IUom = { id: 6576 };
      uomConversion.fromUom = fromUom;
      const toUom: IUom = { id: 6576 };
      uomConversion.toUom = toUom;

      const uomCollection: IUom[] = [{ id: 6576 }];
      vi.spyOn(uomService, 'query').mockReturnValue(of(new HttpResponse({ body: uomCollection })));
      const additionalUoms = [fromUom, toUom];
      const expectedCollection: IUom[] = [...additionalUoms, ...uomCollection];
      vi.spyOn(uomService, 'addUomToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ uomConversion });
      comp.ngOnInit();

      expect(uomService.query).toHaveBeenCalled();
      expect(uomService.addUomToCollectionIfMissing).toHaveBeenCalledWith(
        uomCollection,
        ...additionalUoms.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.uomsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const uomConversion: IUomConversion = { id: 18422 };
      const fromUom: IUom = { id: 6576 };
      uomConversion.fromUom = fromUom;
      const toUom: IUom = { id: 6576 };
      uomConversion.toUom = toUom;

      activatedRoute.data = of({ uomConversion });
      comp.ngOnInit();

      expect(comp.uomsSharedCollection()).toContainEqual(fromUom);
      expect(comp.uomsSharedCollection()).toContainEqual(toUom);
      expect(comp.uomConversion).toEqual(uomConversion);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IUomConversion>();
      const uomConversion = { id: 13785 };
      vi.spyOn(uomConversionFormService, 'getUomConversion').mockReturnValue(uomConversion);
      vi.spyOn(uomConversionService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ uomConversion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(uomConversion);
      saveSubject.complete();

      // THEN
      expect(uomConversionFormService.getUomConversion).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(uomConversionService.update).toHaveBeenCalledWith(expect.objectContaining(uomConversion));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IUomConversion>();
      const uomConversion = { id: 13785 };
      vi.spyOn(uomConversionFormService, 'getUomConversion').mockReturnValue({ id: null });
      vi.spyOn(uomConversionService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ uomConversion: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(uomConversion);
      saveSubject.complete();

      // THEN
      expect(uomConversionFormService.getUomConversion).toHaveBeenCalled();
      expect(uomConversionService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IUomConversion>();
      const uomConversion = { id: 13785 };
      vi.spyOn(uomConversionService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ uomConversion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(uomConversionService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUom', () => {
      it('should forward to uomService', () => {
        const entity = { id: 6576 };
        const entity2 = { id: 20168 };
        vi.spyOn(uomService, 'compareUom');
        comp.compareUom(entity, entity2);
        expect(uomService.compareUom).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
