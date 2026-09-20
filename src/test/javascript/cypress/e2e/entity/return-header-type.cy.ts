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

describe('ReturnHeaderType e2e test', () => {
  const returnHeaderTypePageUrl = '/return-header-type';
  let username: string;
  let password: string;
  const returnHeaderTypeSample = { name: 'indelible quaintly' };

  let returnHeaderType;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/return-header-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/return-header-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/return-header-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (returnHeaderType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/return-header-types/${returnHeaderType.id}`,
      }).then(() => {
        returnHeaderType = undefined;
      });
    }
  });

  it('ReturnHeaderTypes menu should load ReturnHeaderTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('return-header-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ReturnHeaderType').should('exist');
    cy.location('pathname').should('eq', returnHeaderTypePageUrl);
  });

  describe('ReturnHeaderType page', () => {
    it('should have translated page title', () => {
      cy.visit(returnHeaderTypePageUrl);
      cy.getEntityHeading('ReturnHeaderType').should('not.contain', 'snpApp.returnHeaderType.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(returnHeaderTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ReturnHeaderType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${returnHeaderTypePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ReturnHeaderType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', returnHeaderTypePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/return-header-types',
          body: returnHeaderTypeSample,
        }).then(({ body }) => {
          returnHeaderType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/return-header-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [returnHeaderType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(returnHeaderTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ReturnHeaderType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('returnHeaderType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', returnHeaderTypePageUrl);
      });

      it('edit button click should load edit ReturnHeaderType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ReturnHeaderType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', returnHeaderTypePageUrl);
      });

      it('edit button click should load edit ReturnHeaderType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ReturnHeaderType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', returnHeaderTypePageUrl);
      });

      it('last delete button click should delete instance of ReturnHeaderType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('returnHeaderType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', returnHeaderTypePageUrl);

        returnHeaderType = undefined;
      });
    });
  });

  describe('new ReturnHeaderType page', () => {
    beforeEach(() => {
      cy.visit(returnHeaderTypePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ReturnHeaderType');
    });

    it('should create an instance of ReturnHeaderType', () => {
      cy.get(`[data-cy="name"]`).type('viciously tarragon');
      cy.get(`[data-cy="name"]`).should('have.value', 'viciously tarragon');

      cy.get(`[data-cy="description"]`).type('beside honestly inasmuch');
      cy.get(`[data-cy="description"]`).should('have.value', 'beside honestly inasmuch');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        returnHeaderType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', returnHeaderTypePageUrl);
    });
  });
});
