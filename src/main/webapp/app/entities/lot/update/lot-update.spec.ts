import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ILot } from '../lot.model';
import { LotService } from '../service/lot.service';

import { LotFormService } from './lot-form.service';
import { LotUpdate } from './lot-update';

describe('Lot Management Update Component', () => {
  let comp: LotUpdate;
  let fixture: ComponentFixture<LotUpdate>;
  let activatedRoute: ActivatedRoute;
  let lotFormService: LotFormService;
  let lotService: LotService;

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

    fixture = TestBed.createComponent(LotUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    lotFormService = TestBed.inject(LotFormService);
    lotService = TestBed.inject(LotService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const lot: ILot = { id: 5166 };

      activatedRoute.data = of({ lot });
      comp.ngOnInit();

      expect(comp.lot).toEqual(lot);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ILot>();
      const lot = { id: 16504 };
      vi.spyOn(lotFormService, 'getLot').mockReturnValue(lot);
      vi.spyOn(lotService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ lot });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(lot);
      saveSubject.complete();

      // THEN
      expect(lotFormService.getLot).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(lotService.update).toHaveBeenCalledWith(expect.objectContaining(lot));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ILot>();
      const lot = { id: 16504 };
      vi.spyOn(lotFormService, 'getLot').mockReturnValue({ id: null });
      vi.spyOn(lotService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ lot: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(lot);
      saveSubject.complete();

      // THEN
      expect(lotFormService.getLot).toHaveBeenCalled();
      expect(lotService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ILot>();
      const lot = { id: 16504 };
      vi.spyOn(lotService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ lot });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(lotService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
