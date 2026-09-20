import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IPartyClassificationType } from '../party-classification-type.model';
import { PartyClassificationTypeService } from '../service/party-classification-type.service';

import { PartyClassificationTypeFormService } from './party-classification-type-form.service';
import { PartyClassificationTypeUpdate } from './party-classification-type-update';

describe('PartyClassificationType Management Update Component', () => {
  let comp: PartyClassificationTypeUpdate;
  let fixture: ComponentFixture<PartyClassificationTypeUpdate>;
  let activatedRoute: ActivatedRoute;
  let partyClassificationTypeFormService: PartyClassificationTypeFormService;
  let partyClassificationTypeService: PartyClassificationTypeService;

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

    fixture = TestBed.createComponent(PartyClassificationTypeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    partyClassificationTypeFormService = TestBed.inject(PartyClassificationTypeFormService);
    partyClassificationTypeService = TestBed.inject(PartyClassificationTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const partyClassificationType: IPartyClassificationType = { id: 12767 };

      activatedRoute.data = of({ partyClassificationType });
      comp.ngOnInit();

      expect(comp.partyClassificationType).toEqual(partyClassificationType);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPartyClassificationType>();
      const partyClassificationType = { id: 21929 };
      vi.spyOn(partyClassificationTypeFormService, 'getPartyClassificationType').mockReturnValue(partyClassificationType);
      vi.spyOn(partyClassificationTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partyClassificationType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(partyClassificationType);
      saveSubject.complete();

      // THEN
      expect(partyClassificationTypeFormService.getPartyClassificationType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(partyClassificationTypeService.update).toHaveBeenCalledWith(expect.objectContaining(partyClassificationType));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPartyClassificationType>();
      const partyClassificationType = { id: 21929 };
      vi.spyOn(partyClassificationTypeFormService, 'getPartyClassificationType').mockReturnValue({ id: null });
      vi.spyOn(partyClassificationTypeService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partyClassificationType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(partyClassificationType);
      saveSubject.complete();

      // THEN
      expect(partyClassificationTypeFormService.getPartyClassificationType).toHaveBeenCalled();
      expect(partyClassificationTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IPartyClassificationType>();
      const partyClassificationType = { id: 21929 };
      vi.spyOn(partyClassificationTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partyClassificationType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(partyClassificationTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
