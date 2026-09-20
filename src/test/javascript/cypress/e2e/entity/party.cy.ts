import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('Party e2e test', () => {
  const partyPageUrl = '/party';
  let username: string;
  let password: string;
  const partySample = {};

  let party;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/parties+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/parties').as('postEntityRequest');
    cy.intercept('DELETE', '/api/parties/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (party) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/parties/${party.id}`,
      }).then(() => {
        party = undefined;
      });
    }
  });

  it('Parties menu should load Parties page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('party');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Party').should('exist');
    cy.location('pathname').should('eq', partyPageUrl);
  });

  describe('Party page', () => {
    it('should have translated page title', () => {
      cy.visit(partyPageUrl);
      cy.getEntityHeading('Party').should('not.contain', 'snpApp.party.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(partyPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Party page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${partyPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('Party');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/parties',
          body: partySample,
        }).then(({ body }) => {
          party = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/parties+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/parties?page=0&size=20>; rel="last",<http://localhost/api/parties?page=0&size=20>; rel="first"',
              },
              body: [party],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(partyPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Party page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('party');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyPageUrl);
      });

      it('edit button click should load edit Party page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Party');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyPageUrl);
      });

      it('edit button click should load edit Party page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Party');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyPageUrl);
      });

      it('last delete button click should delete instance of Party', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('party').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyPageUrl);

        party = undefined;
      });
    });
  });

  describe('new Party page', () => {
    beforeEach(() => {
      cy.visit(partyPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Party');
    });

    it('should create an instance of Party', () => {
      cy.get(`[data-cy="isOrganisation"]`).should('not.be.checked');
      cy.get(`[data-cy="isOrganisation"]`).click();
      cy.get(`[data-cy="isOrganisation"]`).should('be.checked');

      cy.get(`[data-cy="organisationName"]`).type('making which provided');
      cy.get(`[data-cy="organisationName"]`).should('have.value', 'making which provided');

      cy.get(`[data-cy="organisationShortName"]`).type('subtle spew');
      cy.get(`[data-cy="organisationShortName"]`).should('have.value', 'subtle spew');

      cy.get(`[data-cy="salutation"]`).type('yahoo abaft');
      cy.get(`[data-cy="salutation"]`).should('have.value', 'yahoo abaft');

      cy.get(`[data-cy="firstName"]`).type('Zula');
      cy.get(`[data-cy="firstName"]`).should('have.value', 'Zula');

      cy.get(`[data-cy="middleName"]`).type('openly um obnoxiously');
      cy.get(`[data-cy="middleName"]`).should('have.value', 'openly um obnoxiously');

      cy.get(`[data-cy="lastName"]`).type('Corwin');
      cy.get(`[data-cy="lastName"]`).should('have.value', 'Corwin');

      cy.get(`[data-cy="gender"]`).select('MALE');

      cy.get(`[data-cy="birthDate"]`).type('2023-12-14');
      cy.get(`[data-cy="birthDate"]`).blur();
      cy.get(`[data-cy="birthDate"]`).should('have.value', '2023-12-14');

      cy.get(`[data-cy="primaryPhone"]`).type('abnormally alongside');
      cy.get(`[data-cy="primaryPhone"]`).should('have.value', 'abnormally alongside');

      cy.get(`[data-cy="primaryEmail"]`).type('saveX');
      cy.get(`[data-cy="primaryEmail"]`).should('have.value', 'saveX');

      cy.get(`[data-cy="isTemporaryPassword"]`).should('not.be.checked');
      cy.get(`[data-cy="isTemporaryPassword"]`).click();
      cy.get(`[data-cy="isTemporaryPassword"]`).should('be.checked');

      cy.get(`[data-cy="logoImageUrl"]`).type('ick though');
      cy.get(`[data-cy="logoImageUrl"]`).should('have.value', 'ick though');

      cy.get(`[data-cy="profileImageUrl"]`).type('yowza how inside');
      cy.get(`[data-cy="profileImageUrl"]`).should('have.value', 'yowza how inside');

      cy.get(`[data-cy="notes"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="notes"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="postalAddress"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="postalAddress"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        party = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', partyPageUrl);
    });
  });
});
