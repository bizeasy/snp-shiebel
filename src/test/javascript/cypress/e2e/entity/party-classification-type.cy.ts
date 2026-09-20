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

describe('PartyClassificationType e2e test', () => {
  const partyClassificationTypePageUrl = '/party-classification-type';
  let username: string;
  let password: string;
  const partyClassificationTypeSample = { name: 'ah' };

  let partyClassificationType;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/party-classification-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/party-classification-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/party-classification-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (partyClassificationType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/party-classification-types/${partyClassificationType.id}`,
      }).then(() => {
        partyClassificationType = undefined;
      });
    }
  });

  it('PartyClassificationTypes menu should load PartyClassificationTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('party-classification-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PartyClassificationType').should('exist');
    cy.location('pathname').should('eq', partyClassificationTypePageUrl);
  });

  describe('PartyClassificationType page', () => {
    it('should have translated page title', () => {
      cy.visit(partyClassificationTypePageUrl);
      cy.getEntityHeading('PartyClassificationType').should('not.contain', 'snpApp.partyClassificationType.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(partyClassificationTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PartyClassificationType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${partyClassificationTypePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('PartyClassificationType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyClassificationTypePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/party-classification-types',
          body: partyClassificationTypeSample,
        }).then(({ body }) => {
          partyClassificationType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/party-classification-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [partyClassificationType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(partyClassificationTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PartyClassificationType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('partyClassificationType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyClassificationTypePageUrl);
      });

      it('edit button click should load edit PartyClassificationType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PartyClassificationType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyClassificationTypePageUrl);
      });

      it('edit button click should load edit PartyClassificationType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PartyClassificationType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyClassificationTypePageUrl);
      });

      it('last delete button click should delete instance of PartyClassificationType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('partyClassificationType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyClassificationTypePageUrl);

        partyClassificationType = undefined;
      });
    });
  });

  describe('new PartyClassificationType page', () => {
    beforeEach(() => {
      cy.visit(partyClassificationTypePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PartyClassificationType');
    });

    it('should create an instance of PartyClassificationType', () => {
      cy.get(`[data-cy="name"]`).type('which joint');
      cy.get(`[data-cy="name"]`).should('have.value', 'which joint');

      cy.get(`[data-cy="description"]`).type('fooey fortunately');
      cy.get(`[data-cy="description"]`).should('have.value', 'fooey fortunately');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        partyClassificationType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', partyClassificationTypePageUrl);
    });
  });
});
