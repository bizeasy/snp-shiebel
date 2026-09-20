import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IPartyIdentificationType } from '../party-identification-type.model';
import { PartyIdentificationTypeService } from '../service/party-identification-type.service';

import { PartyIdentificationTypeFormService } from './party-identification-type-form.service';
import { PartyIdentificationTypeUpdate } from './party-identification-type-update';

describe('PartyIdentificationType Management Update Component', () => {
  let comp: PartyIdentificationTypeUpdate;
  let fixture: ComponentFixture<PartyIdentificationTypeUpdate>;
  let activatedRoute: ActivatedRoute;
  let partyIdentificationTypeFormService: PartyIdentificationTypeFormService;
  let partyIdentificationTypeService: PartyIdentificationTypeService;

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

    fixture = TestBed.createComponent(PartyIdentificationTypeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    partyIdentificationTypeFormService = TestBed.inject(PartyIdentificationTypeFormService);
    partyIdentificationTypeService = TestBed.inject(PartyIdentificationTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const partyIdentificationType: IPartyIdentificationType = { id: 4837 };

      activatedRoute.data = of({ partyIdentificationType });
      comp.ngOnInit();

      expect(comp.partyIdentificationType).toEqual(partyIdentificationType);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPartyIdentificationType>();
      const partyIdentificationType = { id: 7941 };
      vi.spyOn(partyIdentificationTypeFormService, 'getPartyIdentificationType').mockReturnValue(partyIdentificationType);
      vi.spyOn(partyIdentificationTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partyIdentificationType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(partyIdentificationType);
      saveSubject.complete();

      // THEN
      expect(partyIdentificationTypeFormService.getPartyIdentificationType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(partyIdentificationTypeService.update).toHaveBeenCalledWith(expect.objectContaining(partyIdentificationType));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPartyIdentificationType>();
      const partyIdentificationType = { id: 7941 };
      vi.spyOn(partyIdentificationTypeFormService, 'getPartyIdentificationType').mockReturnValue({ id: null });
      vi.spyOn(partyIdentificationTypeService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partyIdentificationType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(partyIdentificationType);
      saveSubject.complete();

      // THEN
      expect(partyIdentificationTypeFormService.getPartyIdentificationType).toHaveBeenCalled();
      expect(partyIdentificationTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IPartyIdentificationType>();
      const partyIdentificationType = { id: 7941 };
      vi.spyOn(partyIdentificationTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partyIdentificationType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(partyIdentificationTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
